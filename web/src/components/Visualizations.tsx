import { useEffect, useRef } from 'react';

function parseColor(colorStr: string, alpha: number = 1): string {
    let r = 255, g = 255, b = 255;
    if (colorStr.startsWith('#')) {
        const hex = colorStr.replace('#', '');
        if (hex.length === 6) {
            r = parseInt(hex.slice(0, 2), 16);
            g = parseInt(hex.slice(2, 4), 16);
            b = parseInt(hex.slice(4, 6), 16);
        } else if (hex.length === 8) {
            r = parseInt(hex.slice(2, 4), 16);
            g = parseInt(hex.slice(4, 6), 16);
            b = parseInt(hex.slice(6, 8), 16);
        }
    } else if (colorStr.startsWith('Color(0x')) {
        const hex = colorStr.replace('Color(0x', '').replace(')', '');
        if (hex.length === 8) {
            r = parseInt(hex.slice(2, 4), 16);
            g = parseInt(hex.slice(4, 6), 16);
            b = parseInt(hex.slice(6, 8), 16);
        }
    }
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}

function drawCircle(ctx: CanvasRenderingContext2D, cx: number, cy: number, r: number, color: string, stroke: boolean = false, strokeWidth: number = 1, dashed: boolean = false) {
    if (r < 0) r = 0;
    ctx.beginPath();
    ctx.arc(cx, cy, r, 0, 2 * Math.PI);
    if (dashed) {
        ctx.setLineDash([5, 5]);
    } else {
        ctx.setLineDash([]);
    }
    if (stroke) {
        ctx.strokeStyle = color;
        ctx.lineWidth = strokeWidth;
        ctx.stroke();
    } else {
        ctx.fillStyle = color;
        ctx.fill();
    }
    ctx.setLineDash([]);
}

function drawLine(ctx: CanvasRenderingContext2D, x1: number, y1: number, x2: number, y2: number, color: string, width: number = 1, dashed: boolean = false) {
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    if (dashed) {
        ctx.setLineDash([5, 5]);
    } else {
        ctx.setLineDash([]);
    }
    ctx.strokeStyle = color;
    ctx.lineWidth = width;
    ctx.stroke();
    ctx.setLineDash([]);
}

function solvePingPong(timeMs: number, cycleMs: number): number {
    const t = (timeMs % cycleMs) / cycleMs;
    return t < 0.5 ? t * 2 : 1 - (t - 0.5) * 2;
}

export function NodeVisualization({ nodeId, accentColor }: { nodeId: string, accentColor: string }) {
    const canvasRef = useRef<HTMLCanvasElement>(null);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;
        const ctx = canvas.getContext('2d');
        if (!ctx) return;

        let animationFrameId: number;
        const startTime = Date.now();

        const render = () => {
            const timeMs = Date.now() - startTime;
            const rect = canvas.getBoundingClientRect();
            // Handle resizing
            if (canvas.width !== rect.width || canvas.height !== rect.height) {
                canvas.width = rect.width;
                canvas.height = rect.height;
            }
            
            const w = canvas.width;
            const h = canvas.height;
            ctx.clearRect(0, 0, w, h);
            
            const PI = Math.PI;
            const cos = Math.cos;
            const sin = Math.sin;
            const exp = Math.exp;
            const abs = Math.abs;
            const max = Math.max;

            const cBase = accentColor;
            const white = '#FFFFFF';

            // 1. IntroViz
            if (nodeId === 'intro') {
                const rotation = ((timeMs % 20000) / 20000) * 360;
                const scaleP = solvePingPong(timeMs, 5000);
                const scale = 0.8 + 0.4 * scaleP;
                const cx = w / 2;
                const cy = h / 2;
                const radius = Math.min(w, h) * 0.3 * scale;

                for (let i = 0; i < 16; i++) {
                    const angle = (rotation + i * (360 / 16)) * (PI / 180);
                    const x = cx + cos(angle) * radius;
                    const y = cy + sin(angle) * radius * 0.5;
                    drawCircle(ctx, x, y, 8, parseColor(cBase, 0.6));
                    drawLine(ctx, cx, cy, x, y, parseColor(cBase, 0.2), 2);

                    const nextAngle = (rotation + (i + 1) * (360 / 16)) * (PI / 180);
                    const nx = cx + cos(nextAngle) * radius;
                    const ny = cy + sin(nextAngle) * radius * 0.5;
                    drawLine(ctx, x, y, nx, ny, parseColor(cBase, 0.4), 1);
                }
            }
            // 2. TimeViz
            else if (nodeId === 'time_reality') {
                const sweep = -0.2 + ((timeMs % 6000) / 6000) * 1.4;
                const sweepY = h * sweep;
                for (let i = 0; i <= 20; i++) {
                    for (let j = 0; j <= 10; j++) {
                        const x = w * (i / 20);
                        const y = h * (j / 10);
                        const distToSweep = abs(y - sweepY);
                        const isObserved = distToSweep < 80;
                        const pointAlpha = isObserved ? 1 : 0.15;
                        const pointRadius = isObserved ? 6 : 2;
                        const pointColor = isObserved ? white : parseColor(cBase, pointAlpha);
                        drawCircle(ctx, x, y, pointRadius, pointColor);
                    }
                }
                ctx.fillStyle = parseColor(cBase, 0.3);
                ctx.fillRect(0, sweepY - 40, w, 80);
                drawLine(ctx, 0, sweepY, w, sweepY, white, 4);
            }
            // 3. SpectralViz
            else if (nodeId === 'spectral') {
                const phase = ((timeMs % 3000) / 3000) * 2 * PI;
                const midY = h / 2;
                const freqs = [[0.02, 0.4], [0.05, 0.2], [0.1, 0.1]];
                
                ctx.beginPath();
                for (let x = 0; x < w; x += 4) {
                    let yOffset = 0;
                    for (const [f, a] of freqs) {
                        yOffset += sin(x * f - phase * (f * 10)) * (h * a);
                    }
                    if (x === 0) ctx.moveTo(x, midY + yOffset);
                    else ctx.lineTo(x, midY + yOffset);
                }
                ctx.strokeStyle = parseColor(cBase, 1);
                ctx.lineWidth = 6;
                ctx.stroke();

                freqs.forEach(([f, a], idx) => {
                    ctx.beginPath();
                    const compMidY = h * (0.8 + idx * 0.05);
                    for (let x = 0; x < w; x += 4) {
                        const y = compMidY + sin(x * f - phase * (f * 10)) * (h * a * 0.2);
                        if (x === 0) ctx.moveTo(x, y);
                        else ctx.lineTo(x, y);
                    }
                    ctx.strokeStyle = parseColor(cBase, 0.4);
                    ctx.lineWidth = 2;
                    ctx.stroke();
                });
            }
            // 4. ObserverViz
            else if (nodeId === 'observer') {
                const timePos = 0.2 + solvePingPong(timeMs, 5000) * 0.6;
                const phase = ((timeMs % 1500) / 1500) * 2 * PI;
                const midY = h / 2;
                const windowCenterX = w * timePos;

                ctx.beginPath();
                for (let x = 0; x < w; x += 2) {
                    const dist = x - windowCenterX;
                    const envelope = exp(-(dist * dist) / (2 * 4000));
                    const y = midY + sin(dist * 0.04 - phase) * envelope * (h * 0.25);
                    if (x === 0) ctx.moveTo(x, y);
                    else ctx.lineTo(x, y);
                }
                drawLine(ctx, 0, midY, w, midY, parseColor(cBase, 0.2), 2);
                ctx.strokeStyle = parseColor(cBase, 1);
                ctx.lineWidth = 6;
                ctx.lineCap = 'round';
                ctx.stroke();

                drawCircle(ctx, windowCenterX, midY + sin(-phase) * (h * 0.25), 16, white);
                drawCircle(ctx, windowCenterX, midY, 64, parseColor(cBase, 0.4));
            }
            // 5. HierarchyViz
            else if (nodeId === 'hierarchy') {
                const time = (timeMs % 8000) / 8000;
                const cx = w / 2;
                const cy = h / 2;
                const maxRadius = Math.min(w, h) * 0.45;

                drawCircle(ctx, cx, cy, maxRadius, parseColor(cBase, 0.1 + 0.1 * sin(time * PI * 2)));
                drawCircle(ctx, cx, cy, maxRadius, parseColor(cBase, 0.5), true, 2);

                const nodes = 6;
                for (let i = 0; i < nodes; i++) {
                    const angle = time * PI * 2 + i * (2 * PI / nodes);
                    const r = maxRadius * 0.5;
                    const x = cx + cos(angle) * r;
                    const y = cy + sin(angle) * r;
                    drawCircle(ctx, x, y, r, parseColor(cBase, 0.3));
                    drawCircle(ctx, x, y, 8, parseColor(white, 0.8));
                    drawLine(ctx, cx, cy, x, y, parseColor(cBase, 0.4), 2);
                }
            }
            // 6. HolographyViz
            else if (nodeId === 'holography') {
                const zoom = (timeMs % 4000) / 4000;
                const cx = w / 2;
                const cy = h / 2;

                for (let i = 0; i <= 4; i++) {
                    const scale = (zoom + i) / 5;
                    const radius = Math.min(w, h) * 0.45 * scale;
                    const alpha = Math.max(0, Math.min(1, 1 - scale));

                    drawCircle(ctx, cx, cy, radius, parseColor(cBase, alpha * 0.8), true, 4 * (1 - scale));

                    const detailNodes = 8;
                    if (radius > 20) {
                        for (let d = 0; d < detailNodes; d++) {
                            const angle = d * (2 * PI / detailNodes) + zoom * PI;
                            const x = cx + cos(angle) * radius;
                            const y = cy + sin(angle) * radius;
                            drawCircle(ctx, x, y, 4 * scale, parseColor(white, alpha * 0.5));
                        }
                    }
                }
            }
            // 7. GeometryViz
            else if (nodeId === 'ring') {
                const expansion = 0.1 + ((timeMs % 6000) / 6000) * 1.4;
                const cx = w / 2;
                const cy = h / 2;
                const radius = Math.min(w, h) * 0.4 * expansion;
                const alpha = expansion > 1 ? (1.5 - expansion) / 0.5 : 1;

                drawCircle(ctx, cx, cy, radius, parseColor(cBase, alpha * 0.3), true, 8);

                const observerAngle = Math.min(expansion * 2 * PI, PI / 4 + PI * 2);
                const obsX = cx + cos(observerAngle) * radius;
                const obsY = cy + sin(observerAngle) * radius;
                drawCircle(ctx, obsX, obsY, 16, parseColor(white, alpha));

                const tanLength = w * 0.6;
                const dx = -sin(observerAngle);
                const dy = cos(observerAngle);
                drawLine(ctx, obsX - dx * tanLength, obsY - dy * tanLength, obsX + dx * tanLength, obsY + dy * tanLength, parseColor(cBase, alpha * 0.8), 3);
            }
            // 8. LagrangianPacketViz
            else if (nodeId === 'crit_1_lagrangian') {
                const phase = ((timeMs % 4000) / 4000) * 2 * PI;
                const midY = h / 2;
                const harmonicFreqs = [0.015, 0.03, 0.045, 0.06];
                const harmonicColors = ['#F43F5E', '#FF9800', '#00BCD4', '#E040FB'];

                harmonicFreqs.forEach((freq, idx) => {
                    ctx.beginPath();
                    for (let x = 0; x < w; x += 6) {
                        const y = midY + sin(x * freq - phase * (idx + 1)) * (h * 0.08);
                        if (x === 0) ctx.moveTo(x, y);
                        else ctx.lineTo(x, y);
                    }
                    ctx.strokeStyle = parseColor(harmonicColors[idx], 0.25);
                    ctx.lineWidth = 2;
                    ctx.stroke();
                });

                const packetCenter = w / 2 + sin(phase) * (w * 0.3);
                
                ctx.beginPath();
                for (let x = 0; x < w; x += 3) {
                    const dx = x - packetCenter;
                    const envelope = exp(-(dx * dx) / (2 * 2500));
                    let waveSum = 0;
                    harmonicFreqs.forEach((freq, idx) => {
                        waveSum += sin(x * freq - phase * (idx + 1));
                    });
                    waveSum = (waveSum / harmonicFreqs.length) * (h * 0.35) * envelope;
                    const y = midY + waveSum;
                    if (x === 0) ctx.moveTo(x, y);
                    else ctx.lineTo(x, y);
                }
                ctx.strokeStyle = parseColor(cBase, 1);
                ctx.lineWidth = 5;
                ctx.lineCap = 'round';
                ctx.stroke();

                drawCircle(ctx, packetCenter, midY + sin(packetCenter * 0.03 - phase) * (h * 0.15), 12, white);
                drawCircle(ctx, packetCenter, midY + sin(packetCenter * 0.03 - phase) * (h * 0.15), 28, parseColor(cBase, 0.3));
            }
            // 9. GaborUncertaintyViz
            else if (nodeId === 'crit_2_gabor') {
                const scaleFactor = 0.12 + solvePingPong(timeMs, 3500) * 0.76;
                const phase = ((timeMs % 1500) / 1500) * 2 * PI;
                const dividerX = w * 0.5;

                drawLine(ctx, dividerX, 0, dividerX, h, parseColor(white, 0.15), 2);

                const leftMidY = h * 0.5;
                const leftCenterX = dividerX * 0.5;
                const sigma = 500 + scaleFactor * 9000;

                ctx.beginPath();
                for (let x = 10; x < dividerX - 10; x += 3) {
                    const dx = x - leftCenterX;
                    const envelope = exp(-(dx * dx) / (2 * sigma));
                    const waveVal = sin(dx * 0.13 - phase) * envelope * (h * 0.33);
                    if (x === 10) ctx.moveTo(x, leftMidY + waveVal);
                    else ctx.lineTo(x, leftMidY + waveVal);
                }
                ctx.strokeStyle = parseColor('#FF9800', 1);
                ctx.lineWidth = 3.5;
                ctx.stroke();
                drawCircle(ctx, leftCenterX, leftMidY, 6, white);

                const rightMidY = h * 0.5;
                const rightCenterX = w * 0.75;
                const freqSigma = 32000 / sigma;

                ctx.beginPath();
                for (let x = dividerX + 10; x < w - 10; x += 3) {
                    const dx = x - rightCenterX;
                    const carrierOffset = 40;
                    const specGlow = (exp(-((dx - carrierOffset) * (dx - carrierOffset)) / (2 * freqSigma)) + 
                                      exp(-((dx + carrierOffset) * (dx + carrierOffset)) / (2 * freqSigma))) * 0.5;
                    const peakHeight = specGlow * (h * 0.33) * (1.1 - scaleFactor * 0.5);
                    const y = rightMidY - peakHeight + sin(x * 0.2 - phase * 3) * specGlow * 10;
                    if (x === dividerX + 10) ctx.moveTo(x, y);
                    else ctx.lineTo(x, y);
                }
                drawLine(ctx, dividerX + 20, rightMidY, w - 20, rightMidY, parseColor(white, 0.2), 2);
                ctx.strokeStyle = parseColor(cBase, 1);
                ctx.lineWidth = 4;
                ctx.stroke();

                drawCircle(ctx, rightCenterX + 40, rightMidY, Math.max(10, freqSigma * 0.3), parseColor(cBase, 0.3), true, 1);
                drawCircle(ctx, rightCenterX - 40, rightMidY, Math.max(10, freqSigma * 0.3), parseColor(cBase, 0.3), true, 1);
            }
            // 10. UnitarityRingViz
            else if (nodeId === 'crit_3_unitarity') {
                const radiusMult = 0.45 + solvePingPong(timeMs, 5000) * 0.6;
                const rotation = ((timeMs % 15000) / 15000) * 360;
                
                const cx = w / 2;
                const cy = h * 0.42;
                const baseRadius = Math.min(w, h) * 0.27;
                const currentRadius = baseRadius * radiusMult;

                drawCircle(ctx, cx, cy, 16, parseColor(white, 0.15));
                drawCircle(ctx, cx, cy, baseRadius * 0.45, parseColor(white, 0.1), true, 1, true);
                drawCircle(ctx, cx, cy, baseRadius * 1.05, parseColor(white, 0.1), true, 1, true);
                drawCircle(ctx, cx, cy, currentRadius, parseColor(cBase, 0.3), true, 6);

                const count = 10;
                for (let i = 0; i < count; i++) {
                    const angleDeg = rotation + i * (360 / count);
                    const angleRad = angleDeg * (PI / 180);
                    const sx = cx + cos(angleRad) * currentRadius;
                    const sy = cy + sin(angleRad) * currentRadius;
                    const ampScale = 0.5 + 0.3 * sin(angleDeg * 0.1 + rotation * 0.05);
                    const dotRadius = 10 * ampScale;

                    drawLine(ctx, cx, cy, sx, sy, parseColor(cBase, 0.15), 2);
                    drawCircle(ctx, sx, sy, dotRadius + 6, parseColor(cBase, 0.4));
                    drawCircle(ctx, sx, sy, dotRadius, white);
                }

                const barY = h * 0.88;
                const barW = w * 0.72;
                const barStartX = (w - barW) * 0.5;
                ctx.fillStyle = parseColor(white, 0.1);
                ctx.fillRect(barStartX, barY - 12, barW, 24);
                ctx.fillStyle = parseColor('#10B981', 1);
                ctx.fillRect(barStartX + 2, barY - 10, barW - 4, 20);
                drawLine(ctx, w / 2, barY - 16, w / 2, barY + 16, white, 4);
            }
            // 11. HolographicBekensteinViz
            else if (nodeId === 'crit_4_entropy') {
                const zoom = (timeMs % 4500) / 4500;
                const rotation = ((timeMs % 25000) / 25000) * 360;

                const cx = w / 2;
                const cy = h / 2;
                const boundaryRadius = Math.min(w, h) * 0.42;

                drawCircle(ctx, cx, cy, boundaryRadius, cBase, true, 4);
                drawCircle(ctx, cx, cy, boundaryRadius + 12, parseColor(cBase, 0.15), true, 2);

                const layersCount = 4;
                for (let layer = 0; layer < layersCount; layer++) {
                    const s = (zoom + layer) / layersCount;
                    const scaleRadius = boundaryRadius * s;
                    const alpha = Math.max(0, Math.min(1, 1 - s));

                    if (scaleRadius > 5) {
                        drawCircle(ctx, cx, cy, scaleRadius, parseColor(cBase, alpha * 0.25), true, 2);
                        const segmentCount = 6;
                        const angleOffset = rotation * (layer % 2 === 0 ? 1 : -1);
                        for (let i = 0; i < segmentCount; i++) {
                            const angle = (angleOffset + i * (360 / segmentCount)) * (PI / 180);
                            const px = cx + cos(angle) * scaleRadius;
                            const py = cy + sin(angle) * scaleRadius;
                            drawLine(ctx, cx, cy, px, py, parseColor(white, alpha * 0.3), 1.5);
                            drawCircle(ctx, px, py, 5 * Math.abs(s), parseColor(cBase, alpha * 0.6));
                        }
                    }
                }

                const scanAngle = rotation * 2.2 * (PI / 180);
                const sx = cx + cos(scanAngle) * boundaryRadius;
                const sy = cy + sin(scanAngle) * boundaryRadius;
                drawLine(ctx, cx, cy, sx, sy, parseColor(white, 0.5), 3);
                for (let k = 1; k <= 4; k++) {
                    const f = k / 4;
                    drawCircle(ctx, cx + cos(scanAngle) * boundaryRadius * f, cy + sin(scanAngle) * boundaryRadius * f, 4, white);
                }
            }
            // 12. ZeroSymmetryViz
            else if (nodeId === 'zero_symmetry') {
                const phase = ((timeMs % 3500) / 3500) * 2 * PI;
                const symmetryFactor = solvePingPong(timeMs, 6000);
                
                const cx = w / 2;
                const cy = h / 2;

                for (let i = 0; i <= 10; i++) {
                    drawLine(ctx, (w / 10) * i, 0, (w / 10) * i, h, parseColor(cBase, 0.05), 1);
                }

                ctx.beginPath();
                const pathMinusCtx = new Path2D();
                const pathSumCtx = new Path2D();

                const steps = 120;
                for (let i = 0; i <= steps; i++) {
                    const x = (w / steps) * i;
                    const relX = (i / steps) * 4 * PI;
                    const amp = h * 0.16;
                    const yOffsetF = sin(relX - phase) * amp * (1 - 0.9 * symmetryFactor);
                    const yOffsetMinusF = -sin(relX - phase) * amp * (1 - 0.1 * symmetryFactor);
                    const sumY = yOffsetF + yOffsetMinusF;

                    if (i === 0) {
                        ctx.moveTo(x, cy + yOffsetF);
                        pathMinusCtx.moveTo(x, cy + yOffsetMinusF);
                        pathSumCtx.moveTo(x, cy + sumY);
                    } else {
                        ctx.lineTo(x, cy + yOffsetF);
                        pathMinusCtx.lineTo(x, cy + yOffsetMinusF);
                        pathSumCtx.lineTo(x, cy + sumY);
                    }
                }
                ctx.strokeStyle = parseColor('#00BCD4', 0.7);
                ctx.lineWidth = 3;
                ctx.stroke();

                ctx.strokeStyle = parseColor('#FF9800', 0.7);
                ctx.stroke(pathMinusCtx);

                ctx.setLineDash([15, 10]);
                ctx.strokeStyle = parseColor('#E040FB', 1);
                ctx.lineWidth = 4.5;
                ctx.stroke(pathSumCtx);
                ctx.setLineDash([]);

                const binaryArray = [1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1];
                const binY = h * 0.85;
                const binW = w / (binaryArray.length + 1);
                binaryArray.forEach((bit, i) => {
                    const bx = binW * (i + 1);
                    const bitColor = bit === 1 ? '#10B981' : parseColor(white, 0.2);
                    const bitRadius = bit === 1 ? 8 : 4;
                    drawCircle(ctx, bx, binY, bitRadius, bitColor);
                    if (bit === 1) {
                        drawCircle(ctx, bx, binY, bitRadius + 6 * (1 - symmetryFactor), parseColor(bitColor, 0.3), true, 2);
                    }
                });
            }
            // 13. ScaleSpaceViz
            else if (nodeId === 'scale_space') {
                const attentionYFactor = solvePingPong(timeMs, 4000);
                const rotationAngle = ((timeMs % 8000) / 8000) * 2 * PI;
                
                const cx = w / 2;
                const cy = h / 2;

                for (let i = 1; i <= 4; i++) {
                    drawCircle(ctx, cx, cy, (w * 0.12) * i, parseColor(cBase, 0.03), true, 1);
                }

                drawLine(ctx, cx, h * 0.12, cx, h * 0.88, parseColor('#00BCD4', 0.25), 3, true);

                const levels = 4;
                const levelYPositions = [h * 0.22, h * 0.42, h * 0.62, h * 0.82];
                const levelRadiiX = [w * 0.14, w * 0.24, w * 0.35, w * 0.46];
                const levelRadiiY = [h * 0.04, h * 0.06, h * 0.08, h * 0.10];
                const ringColors = ['#E040FB', '#818CF8', '#22D3EE', '#10B981'];

                const nodeCount = 8;
                for (let n = 0; n < nodeCount; n++) {
                    const angle = (2 * PI / nodeCount) * n + rotationAngle;
                    const cosA = cos(angle);
                    const sinA = sin(angle);
                    ctx.beginPath();
                    for (let lvl = 0; lvl < levels; lvl++) {
                        const px = cx + cosA * levelRadiiX[lvl];
                        const py = levelYPositions[lvl] + sinA * levelRadiiY[lvl];
                        if (lvl === 0) ctx.moveTo(px, py);
                        else ctx.lineTo(px, py);
                    }
                    ctx.setLineDash([6, 6]);
                    ctx.strokeStyle = parseColor(white, 0.08);
                    ctx.lineWidth = 1.5;
                    ctx.stroke();
                    ctx.setLineDash([]);
                }

                for (let lvl = 0; lvl < levels; lvl++) {
                    const rx = levelRadiiX[lvl];
                    const ry = levelRadiiY[lvl];
                    const ly = levelYPositions[lvl];
                    const rColor = ringColors[lvl];
                    
                    const relativeDistance = abs(attentionYFactor - (lvl / (levels - 1)));
                    const highlightIntensity = Math.max(0, Math.min(1, 1 - relativeDistance * 1.8));

                    ctx.beginPath();
                    for (let i = 0; i <= 80; i++) {
                        const theta = (2 * PI / 80) * i;
                        const ex = cx + cos(theta) * rx;
                        const ey = ly + sin(theta) * ry;
                        if (i === 0) ctx.moveTo(ex, ey);
                        else ctx.lineTo(ex, ey);
                    }
                    ctx.strokeStyle = parseColor(rColor, 0.15 + highlightIntensity * 0.70);
                    ctx.lineWidth = 1.5 + highlightIntensity * 3.5;
                    ctx.stroke();

                    if (highlightIntensity > 0.05) {
                        ctx.fillStyle = parseColor(rColor, 0.03 * highlightIntensity);
                        ctx.fill();
                    }

                    for (let n = 0; n < nodeCount; n++) {
                        const angle = (2 * PI / nodeCount) * n + rotationAngle;
                        const nx = cx + cos(angle) * rx;
                        const ny = ly + sin(angle) * ry;
                        const isNodeActive = (n % 3 === lvl % 3);
                        const nodeColor = isNodeActive ? rColor : parseColor(white, 0.35);
                        const nodeRad = isNodeActive ? (5 + highlightIntensity * 4) : 3;
                        drawCircle(ctx, nx, ny, nodeRad, nodeColor);
                        if (isNodeActive && highlightIntensity > 0.1) {
                            drawCircle(ctx, nx, ny, nodeRad + 8 * highlightIntensity, parseColor(rColor, 0.4 * highlightIntensity), true, 1.5);
                        }
                    }
                }

                const activeY = h * 0.22 + attentionYFactor * (h * 0.60);
                drawLine(ctx, cx - w * 0.38, activeY, cx + w * 0.38, activeY, parseColor('#FF9800', 0.45), 2, true);
                drawCircle(ctx, cx, activeY, 10, '#FF9800');
                drawCircle(ctx, cx, activeY, 22, parseColor('#FF9800', 0.35), true, 3);
            }
            // Fallback generic
            else {
                const rotation = ((timeMs % 10000) / 10000) * 360;
                const cx = w / 2;
                const cy = h / 2;
                const radius = Math.min(w, h) * 0.3;

                ctx.beginPath();
                for (let i = 0; i <= 60; i++) {
                    const angle = (rotation + i * (360 / 60)) * (PI / 180);
                    const rx = cx + cos(angle) * radius * (0.8 + 0.2 * sin(angle * 5 + timeMs * 0.005));
                    const ry = cy + sin(angle) * radius * (0.8 + 0.2 * cos(angle * 3 + timeMs * 0.005));
                    if (i === 0) ctx.moveTo(rx, ry);
                    else ctx.lineTo(rx, ry);
                }
                ctx.strokeStyle = parseColor(cBase, 1);
                ctx.lineWidth = 3;
                ctx.stroke();
            }
            
            animationFrameId = requestAnimationFrame(render);
        };
        render();

        return () => cancelAnimationFrame(animationFrameId);
    }, [nodeId, accentColor]);

    return (
        <canvas 
            ref={canvasRef} 
            style={{ width: '100%', height: '100%', display: 'block', borderRadius: '16px', backgroundColor: '#0f172a' }} 
        />
    );
}
