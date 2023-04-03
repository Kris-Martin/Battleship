public enum ErrorType {
    OUT_OF_BOUNDS("Error! You must enter a row between A and J, and" +
        " a column between 1 and 10 for both coordinates.\n"),
    WRONG_LOCATION("Error! Wrong ship location. " +
        "Ship must align horizontally or vertically\n"),
    WRONG_LENGTH("Error! Wrong length! " +
        "The distance from head to tail must equal size of ship.\n"),
    TOO_CLOSE("Error! You placed it too close to another one. Please try again.\n");

    final String message;
    ErrorType(String message) {
        this.message = message;
    }
}
