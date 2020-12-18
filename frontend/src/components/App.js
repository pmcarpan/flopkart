import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Cart from "./Cart";
import Home from "./Home";
import Navbar from "./Navbar";
import ProductDetail from "./ProductDetail";
import Products from "./Products";

function App() {
  return (
    <div>
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
            <Route path="/">
              <Home />
            </Route>
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
