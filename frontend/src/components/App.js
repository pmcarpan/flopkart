import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Cart from "./Cart";
import Home from "./Home";
import Navbar from "./Navbar";
import ProductDetail from "./ProductDetail";
import Products from "./Products";
import Login from "./Login";
import Checkout from "./Checkout";
import { ProvideAuth } from "../auth";

function App() {
  return (
    <div>
      <ProvideAuth>
        <Router>
          <Navbar />
          <div style={{ minWidth: "800px" }}>
            <Switch>
              <Route path="/products/:id">
                <ProductDetail />
              </Route>
              <Route path="/products">
                <Products />
              </Route>
              <Route path="/cart">
                <Cart />
              </Route>
              <Route path="/checkout">
                <Checkout />
              </Route>
              <Route path="/login">
                <Login />
              </Route>
              <Route path="/">
                <Home />
              </Route>
            </Switch>
          </div>
        </Router>
      </ProvideAuth>
    </div>
  );
}

export default App;
