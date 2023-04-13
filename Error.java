public enum Error {
    OUT_OF_BOUNDS("Error! You must enter a row between A and J, and" +
        " a column between 1 and 10 for both coordinates.\n"),
    WRONG_LOCATION("Error! Wrong ship location. " +
        "Ship must align horizontally or vertically\n"),
    WRONG_LENGTH("Error! Wrong length! " +
        "The distance from head to tail must equal size of ship.\n"),
    TOO_CLOSE("Error! You placed it too close to another one. Please try again.\n"),
    OVERLAPPING("Error! You placed it overlapping another ship. Please try again.\n");
    final String message;

    Error(String message) {
        this.message = message;
    }

    public static boolean isOutOfBounds(int x, int y) {
        return x < 0 || x > 9 || y < 0 || y > 9;
    }

    private static boolean isWrongLocation(Point headPos, Point tailPos) {
        return headPos.getX() != tailPos.getX() && headPos.getY() != tailPos.getY();
    }

    private static boolean isWrongLength(Point headPos, Point tailPos, Ship ship) {
        return Point.getDistance(headPos, tailPos) != ship.getSize();
    }

    private static boolean isTooClose(Point headPos, Point tailPos, char[][] gameGrid) {
        Compass facing = Compass.getFacing(headPos, tailPos);

        switch (facing) {
            case WEST -> {
                if (checkLeftRight(headPos, tailPos, gameGrid)) return true;

                for (int y = headPos.getY(); y <= tailPos.getY(); y++) {
                    if (Grid.isCellAboveOrBelowFull(headPos.getX(), y, gameGrid)) return true;
                }
            }
            case EAST -> {
                if (checkLeftRight(headPos, tailPos, gameGrid)) return true;

                for (int y = headPos.getY(); y >= tailPos.getY(); y--) {
                    if (Grid.isCellAboveOrBelowFull(headPos.getX(), y, gameGrid)) return true;
                }
            }
            case NORTH -> {
                if (checkTopBottom(headPos, tailPos, gameGrid)) return true;

                for (int x = headPos.getX(); x <= tailPos.getX(); x++) {
                    if (Grid.isCellLeftOrRightFull(x, headPos.getY(), gameGrid)) return true;
                }
            }
            case SOUTH -> {
                if (checkTopBottom(headPos, tailPos, gameGrid)) return true;

                for (int x = headPos.getX(); x >= tailPos.getX(); x--) {
                    if (Grid.isCellLeftOrRightFull(x, headPos.getY(), gameGrid)) return true;
                }
            }
        }
        return false;
    }

    private static boolean checkLeftRight(Point headPos, Point tailPos, char[][] gameGrid) {
        return Grid.isCellLeftOrRightFull(headPos.getX(), headPos.getY(), gameGrid) ||
               Grid.isCellLeftOrRightFull(tailPos.getX(), tailPos.getY(), gameGrid);
    }

    private static boolean checkTopBottom(Point headPos, Point tailPos, char[][] gameGrid) {
        return Grid.isCellAboveOrBelowFull(headPos.getX(), headPos.getY(), gameGrid) ||
               Grid.isCellAboveOrBelowFull(tailPos.getX(), tailPos.getY(), gameGrid);
    }

    private static boolean isOverlapping(Point headPos, Point tailPos, char[][] gameGrid) {
        Compass facing = Compass.getFacing(headPos, tailPos);

        switch (facing) {
            case WEST -> {
                for (int i = headPos.getY(); i <= tailPos.getY(); i++) {
                    if (gameGrid[headPos.getX()][i] == Cell.SHIP.value) {
                        return true;
                    }
                }
            }
            case EAST -> {
                for (int i = headPos.getY(); i >= tailPos.getY(); i--) {
                    if (gameGrid[headPos.getX()][i] == Cell.SHIP.value) {
                        return true;
                    }
                }
            }
            case NORTH -> {
                for (int i = headPos.getX(); i <= tailPos.getX(); i++) {
                    if (gameGrid[i][headPos.getY()] == Cell.SHIP.value) {
                        return true;
                    }
                }
            }
            case SOUTH -> {
                for (int i = headPos.getX(); i >= tailPos.getX(); i--) {
                    if (gameGrid[i][headPos.getY()] == Cell.SHIP.value) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPresent(Point headPos, Point tailPos, Ship ship, char[][] gameGrid) {
        boolean result = true;
        String message = "";

        if (isOutOfBounds(headPos.getX(), headPos.getY()) ||
            isOutOfBounds(tailPos.getX(), tailPos.getY())
        ) {
            message = OUT_OF_BOUNDS.message;

        } else if (isWrongLocation(headPos, tailPos)) {
            message = WRONG_LOCATION.message;

        } else if (isWrongLength(headPos, tailPos, ship)) {
            message = String.format("%sRemember the %s is %d cells long.\n",
                    WRONG_LENGTH.message,
                    ship.getName(),
                    ship.getSize()
            );
        } else if (isOverlapping(headPos, tailPos, gameGrid)) {
            message = OVERLAPPING.message;

        } else if (isTooClose(headPos, tailPos, gameGrid)) {
            message = TOO_CLOSE.message;

        } else {
            result = false;
        }
        System.out.println(message);
        return result;
    }
}
