import React from "react";
import { Link, useLocation } from "react-router-dom";
import SETTINGS from "../settings";

const { useEffect, useState } = React;

function ProductSummaryCard(props) {
  const {
    id,
    imageUrl,
    name,
    isNewArrival,
    price,
    discountPercentage,
    reducedPrice,
    category,
    footnote,
  } = props;

  const renderPriceSection = () => {
    if (discountPercentage) {
      return (
        <>
          <div className="text-xl text-gray-500 font-medium">
            ₹ {reducedPrice.toLocaleString("en-IN")}
          </div>
          <div className="text-sm text-gray-500 p-1">
            <s>₹ {price.toLocaleString("en-IN")}</s>
          </div>
          <div className="text-sm font-semibold bg-green-300 rounded-sm p-1">
            {discountPercentage}% off
          </div>
        </>
      );
    } else {
      return (
        <>
          <div className="text-xl text-gray-500 font-medium">
            ₹ {price.toLocaleString("en-IN")}
          </div>
        </>
      );
    }
  };

  const renderCategorySection = () => {
    if (isNewArrival) {
      return (
        <>
          <div className="text-sm font-medium text-blue-600">
            Flopkart exclusive
          </div>
          <div className="flex">
            <div className="text-sm bg-yellow-300 rounded-sm p-0.5">
              New in <span className="font-medium">{category}</span>
            </div>
          </div>
        </>
      );
    } else {
      return (
        <>
          <div className="text-sm">
            In <span className="font-medium">{category}</span>
          </div>
        </>
      );
    }
  };

  return (
    <div className="flex p-2 border-b-2 last:border-b-0">
      <div className="max-w-xs w-80">
        <img
          src={`${SETTINGS.STATIC_ROOT_URL}/${imageUrl}`}
          alt={name}
          className="rounded-lg max-h-80 ml-auto mr-auto"
        />
      </div>
      <div className="flex-1 flex flex-col pl-4">
        <div className="font-semibold text-xl">{name}</div>
        <div className="flex">{renderPriceSection()}</div>
        {renderCategorySection()}
        <div className="flex-1" />
        <div className="flex mt-4 mb-4">
          <Link
            to={`/products/${id}`}
            className="rounded-md p-1 border-2 border-blue-200 mr-4 bg-blue-200 hover:bg-blue-500 hover:border-blue-500 hover:text-white font-medium"
          >
            View details
          </Link>
          <Link
            to="/"
            className="rounded-md p-1 border-2 border-blue-500 hover:bg-blue-500 hover:text-white font-medium"
          >
            Add to cart
          </Link>
        </div>
        <div className="text-sm text-gray-500">{footnote}</div>
      </div>
    </div>
  );
}

async function fetchProducts(setProducts, searchQuery) {
  console.log(searchQuery);

  const productsRaw = await fetch(
    `${SETTINGS.BASE_URL}/c/products?action=list`
  );
  const products = await productsRaw.json();

  // TODO: handle queries for [discounted] items and [new] arrivals
  if (searchQuery) {
    const searchMatches = products.filter(
      (product) =>
        product.name.toLowerCase().includes(searchQuery) ||
        product.description.toLowerCase().includes(searchQuery) ||
        product.category.toLowerCase().includes(searchQuery)
    );

    setProducts(searchMatches);
    return;
  }

  setProducts(products);
}

function Products() {
  const [products, setProducts] = useState(null);
  const location = useLocation();
  const searchQuery = new URLSearchParams(location.search).get(
    "productSearchQuery"
  );

  useEffect(() => {
    fetchProducts(setProducts, searchQuery);
  }, [searchQuery]);

  if (!products) {
    return <div></div>;
  }

  const renderOptionalSearchSection = () => {
    if (searchQuery) {
      return (
        <div className="p-2 font-semibold">
          Search results for{" "}
          <span className="border-b-2 border-dashed border-red-600 p-1">
            {searchQuery}
          </span>
        </div>
      );
    }
  };

  const renderProductsList = () => {
    if (products.length === 0) {
      return (
        <div>
          <div className="p-2 text-4xl">No products were found.</div>
          <div className="p-2 text-sm font-medium">
            {searchQuery
              ? "Check your internet or enter another search query."
              : "Check your internet."}
          </div>
        </div>
      );
    }

    return products.map((product) => (
      <ProductSummaryCard key={product.id} {...product} />
    ));
  };

  return (
    <div className="flex">
      <div className="flex-1">
        {renderOptionalSearchSection()}
        {renderProductsList()}
      </div>
    </div>
  );
}

export default Products;
