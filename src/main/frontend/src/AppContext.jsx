import { createContext, useState } from "react";

import PropTypes from "prop-types";

const AppContext = createContext();

const AppContextProvider = ({ children }) => {
  const [itemPrimaryText, setItemPrimaryText] = useState("");

  const updateItemPrimaryText = (primaryText) =>
    setItemPrimaryText(primaryText);

  const contextValue = {
    itemPrimaryText,
    updateItemPrimaryText,
  };

  return (
    <AppContext.Provider value={contextValue}>{children}</AppContext.Provider>
  );
};

AppContextProvider.propTypes = {
  children: PropTypes.element,
};

export { AppContext, AppContextProvider };
