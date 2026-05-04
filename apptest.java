const request = require("supertest");
const app = require("../app");

test("Add product", async () => {
  const res = await request(app)
    .post("/products")
    .send({ name: "Test", price: 10, quantity: 10 });

  expect(res.statusCode).toBe(200);
  expect(res.body.name).toBe("Test");
});

test("Sale reduces stock", async () => {
  const product = await request(app)
    .post("/products")
    .send({ name: "Item", price: 5, quantity: 10 });

  const res = await request(app)
    .post("/sale")
    .send({ productId: product.body.id, quantity: 5 });

  expect(res.body.product.quantity).toBe(5);
});

test("Low stock trigger", async () => {
  const product = await request(app)
    .post("/products")
    .send({ name: "LowItem", price: 5, quantity: 6 });

  const res = await request(app)
    .post("/sale")
    .send({ productId: product.body.id, quantity: 2 });

  expect(res.body.lowStock).toBe(true);
});