Exercise Description
We’d like you to model a scenario in which a shopping cart gets checked out to produce a bill.
The focus here should be on modelling the domain in a maintainable and flexible way.

Producing a strong maintainable domain without finishing the exercise is much better than finishing the
exercise without considering design aspects.

Requirements

The following requirements are presented in sale of importance, so you should ensure the
current requirement is implemented, before moving on with the next one.
Completing the list is not required to pass the interview, so focus on domain modelling, rather than worrying about hurrying up.
Ideally, we would like to witness compiling piece of code.


Please read through all the requirements before starting to take care of the first one.


1. Checking out should produce a receipt, with details of what was bought, with the total
   cost, along with a breakdown of the cost for each cart items.

2. Countable products can be dynamically added to or removed from the cart (add/remove X units of item Y). Examples of these products
   include bottles of milk, and packaged salad.

3. The cart can be checked out to produce a bill against different pricing policies. As an
   example, on some days the product prices can be different.

4. Underage users shouldn’t be able to buy age-restricted products. Examples of these products
   include alcohol, and cigarettes.

5. Uncountable products can be added to the cart (add X grams or kilograms of it). Examples of
   these products include aubergines, courgettes, and onions.

6. Discounts and offers can be applied when checking out a cart. Examples of these offers
   include 3x2, 3 for £10, and buy one get another one for half price.