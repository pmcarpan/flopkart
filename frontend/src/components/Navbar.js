import React from "react";
import { NavLink, useHistory } from "react-router-dom";
import {
  ShoppingCartOutline,
  SearchOutline,
} from "@graywolfai/react-heroicons";

function Navbar() {
  const history = useHistory();

  const handleSearchButtonPress = (event) => {
    event.preventDefault();
    event.stopPropagation();

    const form = event.currentTarget;
    const searchQueryInputField = form.searchQueryInputField;

    const searchQuery = searchQueryInputField.value;

    if (searchQuery) {
      form.reset();
      history.push(`/products?productSearchQuery=${searchQuery}`);
    }
  };

  return (
    <nav
      className="w-full h-16 top-0 flex justify-between py-2 px-6 border-b-2 border-blue-700 bg-gray-200"
      style={{ minWidth: "800px" }}
    >
      <div className="">
        <NavLink exact to="/">
          <span className="font-semibold text-4xl italic text-blue-600">
            Flopkart
          </span>
        </NavLink>
      </div>

      <form
        className="my-auto flex flex-grow mx-8 lg:mx-16"
        onSubmit={handleSearchButtonPress}
      >
        <input
          type="search"
          name="searchQueryInputField"
          placeholder="Search for products"
          autoComplete="off"
          className="flex-grow rounded px-2 py-1 focus:ring-2 focus:ring-blue-500"
        />
        <button className="rounded-full ml-1 h-9 w-9 bg-blue-200 border-2 border-blue-500 hover:bg-blue-500 hover:text-white">
          <SearchOutline className="h-4 w-4 inline" />
        </button>
      </form>

      <div className="my-auto">
        <ul className="flex">
          <li className="mr-2">
            <NavLink
              exact
              activeClassName="border-b-2 border-blue-500"
              className="font-light text-lg mr-4 hover:border-b-2 hover:border-blue-500"
              to="/"
            >
              Home
            </NavLink>
          </li>
          <li className="mr-2">
            <NavLink
              exact
              activeClassName="border-b-2 border-blue-500"
              className="font-light text-lg mr-4 hover:border-b-2 hover:border-blue-500"
              to="/products"
            >
              Products
            </NavLink>
          </li>
          <li>
            <NavLink
              activeClassName="border-b-2 border-blue-500"
              className="font-light text-lg hover:border-b-2 hover:border-blue-500"
              to="/cart"
            >
              Cart <ShoppingCartOutline className="h-4 w-4 inline" />
            </NavLink>
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
