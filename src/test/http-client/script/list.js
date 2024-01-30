client.test('Request executed successfully', () => {
    const validateExpression = !!response.status.toString().match(/20\d/)
    client.assert(validateExpression, 'Response status is not 2xx')
})
client.test("Response 'data' should be a list", () => {
    client.assert(response.body.hasOwnProperty("data"), "Response does not have 'data' key")
    client.assert(Array.isArray(response.body.data), "'data' is not a list")
})