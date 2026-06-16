function App() {
  return (
    <div style={{ padding: '2rem', fontFamily: 'sans-serif', minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <header style={{ borderBottom: '1px solid #1e293b', paddingBottom: '1rem', marginBottom: '2rem' }}>
        <h1 style={{ margin: 0, color: '#e0f2fe' }}>Freque</h1>
        <p style={{ color: '#94a3b8', marginTop: '0.5rem' }}>Частотная Космология - Web Edition</p>
      </header>
      
      <main style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        <div style={{
          backgroundColor: '#1e293b',
          padding: '2rem',
          borderRadius: '12px',
          border: '1px solid #334155'
        }}>
          <h2>Добро пожаловать в web-версию Freque</h2>
          <p style={{ marginTop: '1rem', lineHeight: '1.6', color: '#cbd5e1' }}>
            Это базовая структура React-приложения, развернутая через Vite. 
            Здесь будет располагаться десктопная/планшетная версия "Частотной космологии".
          </p>
          <p style={{ marginTop: '1rem', lineHeight: '1.6', color: '#cbd5e1' }}>
            Деплой на GitHub Pages настроен автоматически через GitHub Actions. Убедитесь, что в настройках репозитория в GitHub включена публикация через Actions.
          </p>
        </div>
      </main>
    </div>
  );
}

export default App;
