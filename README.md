# ParserPlotter

Joint programming assignment SS/2022 HTWK Leipzig.

Program that can parse mathematical expressions with operators *****, **+**, **/**, **^**, **-** and functions **cos()**, **sin()**, **tan()**, **log()**, **sqrt()**, **abs()** and plot their graphs in the x/y Plane.

<img src="./docs/media/preview.png" height="50%">

The viewport of the plane can be scaled around the origin or the mouse pointer and moved by dragging.

## Contributers
- [bubba2k](https://github.com/bubba2k): [Parser](./src/parser/)
- [nessel2nert](https://github.com/nessel2nert): [GUI](./src/gui/)
- [99cardz](https://github.com/99cardz): [Canvas](./src/canvas/)

## Notes

#### Parser

Mogensen, T. (2010). _Basics Of Compiler Design_ <br>
Aho, A. V., Lam, M. S., Sethi, R., Ullman, J. D. (2007). _Compilers - Principles, Techniques, & Tools (Second Edition)_

_Recursive Descent Parsing_ by hhp3 - https://youtu.be/SToUyjAsaFk <br>
Java Docs - https://docs.oracle.com/en/java/ <br>
_Regular Expressions in Java_ by Lars Vogel https://www.vogella.com/tutorials/JavaRegularExpressions/article.html <br>

Other bits: <br>
https://stackoverflow.com/questions/8938498/get-the-index-of-a-pattern-in-a-string-using-regex <br>

#### Function Limits

During plotting each horizontal pixel on the canvas and their corresponding x value gets a calculated y value and therefore a point on the canvas. Between each point a line will get drawn. However, when the function encounters a limit where the y value approaches positive or negative infinity, points should not be connected. Because the analysis for limits of the function requires the derivatives of the function which would leave scope of the assignment we chose a simpler approach. 

The array of x Values that represent each horizontal pixels value to be turned into y Values will get evaluated in bulk by the syntax tree provided by the parser. This allows for a targeted search for known limits of the implemented operators and functions. For example, when the devisor in a division passes 0, we can assume, checking for some edge cases, that the function between these points passes a limit. The computed y Values at these points will then get 'corrected' to their respective limits and accounted for during drawing.