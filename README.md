# WalmartShopper

The Walmart Shopper app displays available products in a two column format with the product image and product name,

Clicking on a product will display product details. Swiping to next items is enabled. However sweeping to the previous item is not currently enabled although the previous items will be reached by continually sweeping to the right.

Each fetch of products requests a new page and the maximum of 30 products.

Known issues:

- Image loading on the detail page is slow.
- When new data is loaded, the page automatically scrolls to the top instead of staying at the current position.
- New data loading is jumpy.
- Loading is very slow on a Nexus 6P
