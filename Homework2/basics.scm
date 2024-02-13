#lang scheme
; Semicolon is used for writing comments

; 1- everything should be encloused in parenthesis
; 2- Scheme is a prefix notation (also known as Polish notation),
;    where the operator/function comes before the operands/parameters
;    within an expression

; 1- defining variables

;    1-1 - primitive types:
;        a- numerics -> 12  45.5
;        b- boolean -> #t (true) #f (false)
;        c- character -> #\a #\M
;        d- string -> "hello"
;    1-2 - compound types
;        such Pairs and Lists, Vectors, etc.

(define x 10)
(define str "hello")
(define num 34.44)
(define z #\a) ;character, can not define function(variable) name already defined
; *******
; This is not really a variable
; this is still a function that is holding a value
; *******

; 2- print\display values
;    1- display is designed for producing human-readable output,
;       particularly for strings and characters.
;    2- print is more oriented towards producing output that is
;       informative for the programmer, including additional details
;       about the types and structures of the values it outputs.
(display z)
(newline) ; (new feed) take the curser to the new line
; OR
(display #\newline) ; we can use characater newline -> #\newline
(print z)
(newline)

; parenthesis can be used nested
(display "Meisam")
;; for space we can use characater space -> #\space or string " "
(display " ")
(display #\space)

(display "Amjad")
(newline)

(display str)
(newline) 
(print str)
(newline)

; for numbers, both print the same thing
(display num)
(newline) 
(print num)
(newline)
(display "----------")
(newline)

(display #t)
(newline)
(print #f)
(newline)

; 3- arithmatic operators
;   + - * /
;   modulo  -> fot getting remainder
;   quotient -> returns the int part of the division
;      e.g (/ 5 2) is 2.5, the quotient function return only 2

(display (+ 12 100))
(newline)

(display (+ 13 (* (- 3) 4)))
(newline)

(define result (* num 2))
(display (- num 1))
(newline)

; --> devision
;; since both are integer it return the exact rational result
;; don't forget Scheme tries to maintain the exactness of numbers
(display (/ 5 2)) 
(newline)
(print (/ 5 2)) ; same but with more details
(newline)
(display (/ 5 2.0)) ; to get the real result we need at least one floating-point number
(newline)

(display "remainder: ")
(display (modulo 5 2))
(newline)
(display "integer part: ")
(display (quotient 5 2))
(newline)


; ==> extra functions
; even?
; odd?
; zero?
; negative?
; sqrt
(define n 2)
(display (even? n))
(newline)
(display (odd? n))
(newline)
(display (zero? n))
(newline)
(display (negative? -4))
(newline)
(display (negative? n))
(newline)
(display (sqrt 4))
(newline)




; 4- Defining actual functions
;    4-1- with no parameter
(define (printDashes)
  (newline)
  (display "------------")
  (newline)
)

(printDashes)

;    4-2- With parameters
(define (sum a b)
  (+ a b)
)

(display (sum 20 4))
(newline)

(define (demo)
	(display (+ 2 3))
	(newline)
	(display (acos -1.0))
	(newline)
	(display (abs -232))
	(newline)
	(display (+ (* 2 4) 3))
	(newline)
	(display (+ 3 (* 2 4)))
	(newline)
)

(printDashes)
(demo)

; 5- IF statement
;    Logical operators: = < > <= >= <>
;                       <> is not equal
;    (if  (condition) retuns if true returns if false)

(display (if (even? n) "even" (+ 3 4)))
; if n is even, it returns "even", else it returns the sum of 3 and 4

; 6- multiple conditions
;    such as if and else if
;    (cond
;        ((condition 1) return value)
;        ((condition 2) return value)
;        (so on..)
;        (else return value)
;    )
(define (fib n)
	(cond
		((= n 1) 1)
		((= n 2) 1)
		(else (+ (fib (- n 1)) (fib (- n 2))))
	)
)

;; PRACTICE
;; Write functions 


(define (sqr x) (* x x)) ; written for you


(define (max a b)
  ; FILL THIS IN using if
  0
)

(define (average-of-three a b c)
  ; FILL THIS IN
  0
)

(define (number-sign n)
  ; FILL THIS IN using cond
  ; return "positive" if n is positive
  ; return "negative" if n is negative
  ; return "zero" if n is zero
  0
)
