import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import ItemView from './ItemView'
import BasketView from './BasketView'
import './index.css'

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
     <ItemView />
     <BasketView />
  </React.StrictMode>,
)
