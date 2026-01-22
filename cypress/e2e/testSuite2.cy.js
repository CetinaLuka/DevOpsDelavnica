describe('preverjanja', () => {

  beforeEach(() => {
    cy.visit('http://localhost:3000')
  })

  it('preveri-iskanje', () => {
    cy.get('[href="/products"] button').click()

    cy.log('writting into the search box')
    cy.get('#productSearchBox').type('cipresa{enter}')
    
    cy.log('retrieve table rows')
    const trList = cy.get('tbody tr')
    trList.should('have.length', 0)
    trList.should('not.exist')
  })

  it('preveri url products', () => {
    cy.pause()
    cy.get('[href="/products"] button').click()
    cy.pause()
    cy.url().should('include', '/products')
  })
})