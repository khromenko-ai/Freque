import { useState, useRef, useEffect } from 'react';

// Similar to Visualizations.tsx parseColor but simpler for this specific use case
function parseColor(colorStr: string, alpha: number = 1): string {
    let r = 255, g = 255, b = 255;
    if (colorStr.startsWith('#')) {
        const hex = colorStr.replace('#', '');
        if (hex.length === 6) {
            r = parseInt(hex.slice(0, 2), 16);
            g = parseInt(hex.slice(2, 4), 16);
            b = parseInt(hex.slice(4, 6), 16);
        }
    }
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}

export function PixelFractalViz({ color, zoomFactor }: { color: string, zoomFactor: number }) {
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
            const time = (timeMs % 4000) / 4000; // 0 to 1

            const rect = canvas.getBoundingClientRect();
            if (canvas.width !== rect.width || canvas.height !== rect.height) {
                canvas.width = rect.width;
                canvas.height = rect.height;
            }

            const w = canvas.width;
            const h = canvas.height;
            ctx.clearRect(0, 0, w, h);

            const cx = w / 2;
            const cy = h / 2;
            const baseSize = Math.min(w, h) * 0.8;

            const drawFractal = (x: number, y: number, sz: number, depth: number, maxDepth: number) => {
                if (depth > maxDepth || sz < 1) return;

                // Draw bounding box / wave packet border
                ctx.beginPath();
                const radius = sz * 0.1;
                ctx.roundRect(x - sz / 2, y - sz / 2, sz, sz, radius);
                ctx.strokeStyle = parseColor(color, 0.3 / (depth + 1));
                ctx.lineWidth = Math.max(1, sz * 0.02);
                ctx.stroke();

                const subSz = sz / 3;
                for (let i = -1; i <= 1; i++) {
                    for (let j = -1; j <= 1; j++) {
                        const px = x + i * subSz;
                        const py = y + j * subSz;
                        
                        if (i === 0 && j === 0) {
                            drawFractal(px, py, subSz, depth + 1, maxDepth);
                        } else {
                            const pulse = (Math.sin(time * 2 * Math.PI + depth + i + j) + 1) / 2;
                            ctx.beginPath();
                            ctx.arc(px, py, subSz * 0.3, 0, Math.PI * 2);
                            ctx.fillStyle = parseColor(color, 0.5 * pulse);
                            ctx.fill();
                        }
                    }
                }
            };

            ctx.save();
            ctx.translate(cx, cy);
            ctx.scale(zoomFactor, zoomFactor);
            ctx.translate(-cx, -cy);

            const maxDepth = Math.floor(Math.log(zoomFactor + 1.0) / Math.log(3.0) * 2 + 2);
            drawFractal(cx, cy, baseSize, 0, maxDepth);

            ctx.restore();

            animationFrameId = requestAnimationFrame(render);
        };
        render();

        return () => cancelAnimationFrame(animationFrameId);
    }, [color, zoomFactor]);

    return (
        <canvas 
            ref={canvasRef} 
            style={{ width: '100%', height: '100%', display: 'block', borderRadius: '16px', backgroundColor: '#0f172a' }} 
        />
    );
}

export function PixelExperiment() {
    const [zoomFactor, setZoomFactor] = useState(1);

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100%', padding: '1rem', color: 'white' }}>
            <h2 style={{ color: '#F43F5E', marginTop: 0, marginBottom: '0.5rem' }}>Эксперимент</h2>
            <h3 style={{ margin: '0 0 1rem 0' }}>«Увеличение разрешения пикселя»</h3>
            <p style={{ color: '#cbd5e1', lineHeight: '1.6', marginBottom: '1.5rem' }}>
                Этот эксперимент проверяет гипотезу голографических подобий. Увеличение разрешения одного «пикселя» раскрывает рекурсивные структуры (фрактальные гармоники), показывая, что макро и микро уровни вложены.
            </p>

            <div style={{ flex: 1, border: '1px solid #F43F5E40', borderRadius: '16px', overflow: 'hidden', minHeight: '300px' }}>
                <PixelFractalViz color="#F43F5E" zoomFactor={zoomFactor} />
            </div>

            <div style={{ marginTop: '2rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
                <span style={{ width: '100px' }}>Увеличение:</span>
                <input 
                    type="range" 
                    min="1" max="10" step="0.1" 
                    value={zoomFactor} 
                    onChange={(e) => setZoomFactor(parseFloat(e.target.value))}
                    style={{ flex: 1, accentColor: '#F43F5E' }} 
                />
                <span style={{ width: '50px', textAlign: 'right', color: '#F43F5E', fontWeight: 'bold' }}>{zoomFactor.toFixed(1)}x</span>
            </div>
            <p style={{ textAlign: 'center', color: '#94a3b8', fontSize: '0.85rem', marginTop: '0.5rem' }}>
                Используйте ползунок для рекурсивного масштабирования
            </p>
        </div>
    );
}
