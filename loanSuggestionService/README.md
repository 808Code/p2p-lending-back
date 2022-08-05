### Loan Suggestion Service

1. Fetch loan request from the queue
2. Checks the type of borrower(i.e. Red or Green)
3. Discard request if borrower is red, proceed if green
4. Search for relevant lenders on the basis of last active status
5. Link lenders to loan request by preparing loan suggestion table
6. Show loan requests to lenders on the basis of loan suggestion table