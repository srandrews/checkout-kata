### Checkout Module

A module to load stock item details from a CSV file and generate the total value of 
the order based on the items that have been scanned.

### Assumptions

I have assumed that all quantities should be > 0 and prices >= 0 are acceptable.

### Usage

See CheckoutIT for usage of the DefaultCheckout module.

### Improvements

If I had more time I would make the following improvements:

The CSV file loader is rigid in the handling of the files, this could be improved 
(ideally by using a CSV library, such as opencsv)

The checkout process assumes that StockItem prices stay constant, any updates to the StockItem 
pricing, during a checkout tranaction could lead to inconsistent results. Going forward, only the sku ID should
be stored in the OrderItem and the price looked up when requesting the order total.

The thrown exceptions need to be simplified and documented to detail when each exception can be thrown
