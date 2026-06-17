import { useState, useEffect } from 'react';
import { FirstSourceReader } from './components/FirstSource';
import { MainTabWorkspace } from './components/MainTab';
import { WhitePaperReader } from './components/WhitePaper';
import { Criticism } from './components/Criticism';

function App() {
  const [selectedTab, setSelectedTab] = useState(1);
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
    const checkMobile = () => setIsMobile(window.innerWidth < 768);
    checkMobile();
    window.addEventListener('resize', checkMobile);
    return () => window.removeEventListener('resize', checkMobile);
  }, []);

  const tabs = [
    { id: 0, title: 'Базовый', icon: '📖', color: '#4CAF50', desc: 'Первоисточник и базовые основы теории Частотной Космологии.' },
    { id: 1, title: 'Основной', icon: '🧭', color: '#00BCD4', desc: 'Основной раздел: интерактивные исследования и эссе.' },
    { id: 2, title: 'Контекст', icon: '📚', color: '#E040FB', desc: 'Манифест проекта и общий контекст экосистемы.' },
    { id: 3, title: 'Критика', icon: '⚖️', color: '#FF9800', desc: 'Научная критика, анализ и дискуссионные материалы.' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: isMobile ? 'column' : 'row', minHeight: '100vh', backgroundColor: '#0f172a', color: 'white', fontFamily: 'sans-serif' }}>
      
      {/* Navigation (Rail for Desktop, Bottom Bar for Mobile) */}
      <nav style={{ 
        width: isMobile ? '100%' : '100px', 
        height: isMobile ? '80px' : 'auto',
        backgroundColor: '#1e293b', 
        display: 'flex', 
        flexDirection: isMobile ? 'row' : 'column', 
        alignItems: 'center', 
        justifyContent: isMobile ? 'space-around' : 'flex-start',
        paddingTop: isMobile ? '0' : '2rem',
        borderRight: isMobile ? 'none' : '1px solid #334155',
        borderTop: isMobile ? '1px solid #334155' : 'none',
        position: isMobile ? 'fixed' : 'static',
        bottom: 0,
        zIndex: 10
      }}>
        {/* Map FAB (Only on desktop or placed differently on mobile) */}
        {!isMobile && (
          <button 
            style={{ 
              backgroundColor: '#00BCD4', color: 'white', border: 'none', borderRadius: '16px', 
              width: '56px', height: '56px', cursor: 'pointer', fontSize: '1.5rem', marginBottom: '3rem',
              boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'
            }}
            title="Карта смыслов"
            onClick={() => alert('Интерактивная карта смыслов будет портирована сюда.')}
          >
            🗺️
          </button>
        )}

        {tabs.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setSelectedTab(tab.id)}
            style={{
              backgroundColor: 'transparent',
              border: 'none',
              color: selectedTab === tab.id ? tab.color : '#94a3b8',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              cursor: 'pointer',
              marginBottom: isMobile ? '0' : '2rem',
              transition: 'all 0.2s',
              opacity: selectedTab === tab.id ? 1 : 0.6,
              transform: selectedTab === tab.id ? 'scale(1.1)' : 'scale(1)'
            }}
          >
            <span style={{ fontSize: isMobile ? '1.5rem' : '1.75rem', marginBottom: '0.25rem' }}>{tab.icon}</span>
            <span style={{ fontSize: '0.7rem', fontWeight: selectedTab === tab.id ? 'bold' : 'normal' }}>{tab.title}</span>
          </button>
        ))}
      </nav>

      {/* Main Content Area */}
      <main style={{ 
        flex: 1, 
        padding: isMobile ? '2rem 1.5rem 100px 1.5rem' : '3rem', 
        display: 'flex', 
        flexDirection: 'column' 
      }}>
        <header style={{ borderBottom: '1px solid #334155', paddingBottom: '1rem', marginBottom: '2rem' }}>
          <h1 style={{ margin: 0, color: tabs[selectedTab].color, display: 'flex', alignItems: 'center', gap: '1rem', fontSize: isMobile ? '1.5rem' : '2rem' }}>
            <span>{tabs[selectedTab].icon}</span>
            Freque / {tabs[selectedTab].title}
          </h1>
        </header>

        <section style={{ 
          backgroundColor: '#0f172a', 
          flex: 1,
          overflowY: 'auto',
          padding: isMobile ? '1rem' : '2rem',
          borderRadius: '24px',
          border: `1px solid ${tabs[selectedTab].color}40`,
          boxShadow: `0 10px 30px -10px ${tabs[selectedTab].color}20`
        }}>
          {selectedTab === 0 && <FirstSourceReader />}
          {selectedTab === 1 && <MainTabWorkspace />}
          {selectedTab === 2 && <WhitePaperReader />}
          {selectedTab === 3 && <Criticism />}
        </section>
      </main>

      {/* Mobile Map FAB Floating */}
      {isMobile && (
        <button 
          style={{ 
            backgroundColor: '#00BCD4', color: 'white', border: 'none', borderRadius: '16px', 
            width: '56px', height: '56px', cursor: 'pointer', fontSize: '1.5rem',
            boxShadow: '0 4px 12px rgba(0,0,0,0.3)',
            position: 'fixed', bottom: '100px', right: '1.5rem', zIndex: 20
          }}
          title="Карта смыслов"
          onClick={() => alert('Интерактивная карта смыслов будет портирована сюда.')}
        >
          🗺️
        </button>
      )}

    </div>
  );
}

export default App;
