import React from 'react'
import LibraryCards from './LibraryCards'
import LibraryHeader from './LibraryHeader'
function LibraryBooks() {
    const books = [
        {
            code:"WEB 001",
            name: "Web Development with React",
            price: 29.99,
        },
        {
            code:"CSS 002",
            name: "Web Development with Css",
            price: 39.99,
        },
        {
            code:"JS 003",
            name: "JavaScript for beginners",
            price: 49.99,
        }

    ]
  return (
    <>
    <div>
      <LibraryHeader />
      <h2>List of Books</h2>
      <ul>
        {books.map(book =><LibraryCards bookInfo={book}/>)}
      </ul>
    </div>
     <div>
      The total price for {books.length} is: {books.reduce((total, book) => total + book.price, 0)}
      </div> 
      </>
  )
}

export default LibraryBooks
