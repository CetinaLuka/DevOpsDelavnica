describe('upravljanje meritev', () => {

  beforeEach(() => {
    cy.visit('http://localhost:3000')
  })

  it('add a measurement', ()=>{
    cy.get('[data-cy="addMeasurementButton"]').click() //best practice
        cy.get('#demo-simple-select').click()
        cy.get('[role="listbox"]').contains('Milka Classic').then(item => {
            let id = item.attr('data-value')
            cy.wrap(item).click()
            cy.get('#avgTemp').type('19')
            cy.get('#submitMeasurementButton').click()
            cy.get('[href="/products"] button').click()

            cy.get('tbody tr th').contains(id).then(th => {
                const min = th.parent().children('[id^="productsTableBodyMin"]').get(0).textContent
                const max = th.parent().children('[id^="productsTableBodyMax"]').get(0).textContent

                cy.get('[href="/"] button').click()

                cy.get('tbody tr').each((tr) => {
                    const rowId = tr.children('[id^="measurementsTableBodyProductId"]').get(0).textContent
                    const avgTemp = tr.children('[id^="measurementsTableBodyAverage"]').get(0).textContent
                    if (rowId === id) {
                        const temperatureCell = cy.wrap(tr).children('[id^="measurementsTableBodyTempOK"]')
                        if (avgTemp >= min && avgTemp <= max) {
                            temperatureCell.find('svg[data-testid="DoneIcon"]').should('exist')
                        } else {
                            temperatureCell.find('svg[data-testid="CloseIcon"]').should('exist')
                        }
                    }
                })
            })
        })
  })
})