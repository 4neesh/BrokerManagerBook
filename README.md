# Order Book Manager
An Order book is used by an exchange to maintain and match a list of orders from buyers and sellers. Order books for different instruments are separate.
Each order book consists of two sides – bid and ask. The bid side is the set of orders from buyers and the ask side is the set of orders from sellers.
Each side consists of price levels, where orders from each side of the book with the same price are grouped together.
The levels within the book side are sorted by price in such way that the top level for the bid side contains orders with the highest price, and the top level for the ask side contains orders with the lowest price.

Inside each level, orders are sorted in the order as they arrive. The operations that can be performed on the books:
<ul>
<li>Add new order.</li>
<li>Modify existing order. If the quantity is increased, the order is put at the end of the queue of orders for the relevant price level.</li>
<li>Delete existing order.</li>
</ul>

Each order book provides the following information:
<ul>
<li>Best bid and best ask prices</li>
<li>Number of orders on a level and side of the book</li>
<li>Total tradeable quantity on a level and side of the book</li>
<li>Total tradeable volume, i.e. sum of price * quantity for all orders, on a level and side of the book</li>
<li>List of orders on a level and side of the book, in the correct order</li>
</ul>

# Motivation
This is a vanilla java application that uses data structures efficiently to manage and store orders.

# Tech/Framework
<ul>
  <li>Java 8</li>
  <li>TDD</li>
  <li>JUnit</li>
</ul>
