import { useState } from 'react';

function App() {
  const [selectedTab, setSelectedTab] = useState(1);

  const tabs = [
    { id: 0, title: 'Базовый', icon: '📖', color: '#4CAF50', desc: 'Первоисточник и базовые основы теории.' },
    { id: 1, title: 'Основной', icon: '🧭', color: '#00BCD4', desc: 'Основной раздел исследования.' },
    { id: 2, title: 'Контекст', icon: '📚', color: '#E040FB', desc: 'Манифест и общий рабочий контекст.' },
    { id: 3, title: 'Критика', icon: '⚖️', color: '#FF9800', desc: 'Научная критика и анализ.' },
  ];

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: '#0f172a', color: 'white', fontFamily: 'sans-serif' }}>
      
      {/* Navigation Rail for Tablet/Desktop */}
      <nav style={{ 
        width: '100px', 
        backgroundColor: '#1e293b', 
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        paddingTop: '2rem',
        borderRight: '1px solid #334155'
      }}>
        {/* Map FAB */}
        <button 
          style={{ 
            backgroundColor: '#00BCD4', 
            color: 'white', 
            border: 'none', 
            borderRadius: '16px', 
            width: '56px', 
            height: '56px', 
            cursor: 'pointer',
            fontSize: '1.5rem',
            marginBottom: '3rem',
            boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'
          }}
          title="Карта смыслов"
          onClick={() => alert('Карта смыслов откроется здесь.')}
        >
          🗺️
        </button>

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
              marginBottom: '2rem',
              transition: 'color 0.2s',
              opacity: selectedTab === tab.id ? 1 : 0.7
            }}
          >
            <span style={{ fontSize: '1.75rem', marginBottom: '0.25rem' }}>{tab.icon}</span>
            <span style={{ fontSize: '0.75rem', fontWeight: selectedTab === tab.id ? 'bold' : 'normal' }}>{tab.title}</span>
          </button>
        ))}
      </nav>

      {/* Main Content Area */}
      <main style={{ flex: 1, padding: '3rem', display: 'flex', flexDirection: 'column' }}>
        <header style={{ borderBottom: '1px solid #334155', paddingBottom: '1rem', marginBottom: '2rem' }}>
          <h1 style={{ margin: 0, color: tabs[selectedTab].color, display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <span>{tabs[selectedTab].icon}</span>
            Freque — {tabs[selectedTab].title}
          </h1>
        </header>

        <section style={{ 
          backgroundColor: '#1e293b', 
          padding: '2rem', 
          borderRadius: '16px', 
          border: `1px solid ${tabs[selectedTab].color}40`,
          flex: 1
        }}>
          <h2 style={{ color: '#e2e8f0', marginTop: 0 }}>Раздел в разработке</h2>
          <p style={{ color: '#94a3b8', lineHeight: '1.6', maxWidth: '600px' }}>
            Вы находитесь в Web-версии приложения <b>Freque (Частотная Космология)</b>. <br/><br/>
            {tabs[selectedTab].desc} <br/><br/>
            Данный интерфейс визуализирует структуру планшетной версии Native Android-приложения. Дальнейший функционал будет постепенно портирован в React.
          </p>
        </section>
      </main>

    </div>
  );
}

export default App;
