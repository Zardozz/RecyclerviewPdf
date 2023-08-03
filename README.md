<div align="center">
    <h2>Pdf of Recyclerview Example</h2>
</div>

This is an basic example of how to create a multi page PDF from a RecyclerView by drawing views to the built in PdfDocument class.

Note that you need to treat a pdf page as a large low resolution screen, much as your app has adapt to display on different screen sizes.

This is because an A4 pdf page is only 594 postscript (pixels) wide and 841 594 postscript (pixels) high. Thus you probably want to use much smaller font sizes than you would for a devices screen (This basic example does not adjust for the pdf size)

The key is to pre-measure all the recycler items (not just the ones that the recycler needs to show on the screen) and thus you can build a view to draw to the pdf that fits the pdf page.

This uses a vertical LinearLayout instead of a vertical LinearLayoutManager to show all the items (but they arrange items in the same fashion)

Note you should really do the PDF generation in the background because it can take some time as it draws all items in the recyclerview.

This example produces a basic recycler view
<p align="center">
      <img src="https://raw.githubusercontent.com/Zardozz/RecyclerviewPdf/master/art/recycler_screen.png">
</p>

And then a multi page pdf
<p align="center">
      <img src="https://raw.githubusercontent.com/Zardozz/RecyclerviewPdf/master/art/pdf_screen.png">
</p>

The Skia PDF graphics driver uses PDF Text items for Text, PDF lines for lines, etc.

As you can see in the below image Adobe Acrobat sees the Textview's Text as a Editable Text item.

<p align="center">
      <img src="https://raw.githubusercontent.com/Zardozz/RecyclerviewPdf/master/art/pdf.png">
</p>

## License

```
MIT License

Copyright (c) 2023 Andrew Beck

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
