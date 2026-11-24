import React from 'react'
import { Link } from 'react-router'

function LibraryHeader() {
  return (
    <div>
       <header>
        <h1>Library Management System</h1>
        <Link to="/books">Books</Link>
        <Link to="/office">Office</Link>
        <Link to="/doctor">Doctor</Link>
        <Link to="/">LOGOUT</Link>
       </header>
    </div>
  )
}

export default LibraryHeader

