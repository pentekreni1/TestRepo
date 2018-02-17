package gui;

import game.AiGameBoard;
import game.Constant;
import game.Game;
import game.Observer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Checker;
import model.Die;
import model.Move;
import states.MoveValidationMethods;

import java.util.*;
import java.util.Map.Entry;

public class AiGameController implements Observer {

    private AnchorPane ap;

    @FXML
    Rectangle stack0;
    @FXML
    Polygon stack1;
    @FXML
    Polygon stack2;
    @FXML
    Polygon stack3;
    @FXML
    Polygon stack4;
    @FXML
    Polygon stack5;
    @FXML
    Polygon stack6;
    @FXML
    Polygon stack7;
    @FXML
    Polygon stack8;
    @FXML
    Polygon stack9;
    @FXML
    Polygon stack10;
    @FXML
    Polygon stack11;
    @FXML
    Polygon stack12;
    @FXML
    Polygon stack13;
    @FXML
    Polygon stack14;
    @FXML
    Polygon stack15;
    @FXML
    Polygon stack16;
    @FXML
    Polygon stack17;
    @FXML
    Polygon stack18;
    @FXML
    Polygon stack19;
    @FXML
    Polygon stack20;
    @FXML
    Polygon stack21;
    @FXML
    Polygon stack22;
    @FXML
    Polygon stack23;
    @FXML
    Polygon stack24;
    @FXML
    Rectangle stack25;
    @FXML
    Rectangle stack26;
    @FXML
    Rectangle stack27;
    @FXML
    ImageView die1;
    @FXML
    ImageView die2;
    @FXML
    ImageView die3;
    @FXML
    ImageView die4;
    @FXML
    ImageView background;
    @FXML
    javafx.scene.control.MenuBar menuBar;
    @FXML
    Button btnRoll;
    @FXML
    Button btnOK;
    @FXML
    VBox myToggleGroup;
    @FXML
    Circle goRed;
    @FXML
    Circle goBlack;
    @FXML
    Text redBorneOffCount;
    @FXML
    Text blackBorneOffCount;
    @FXML
    Text informationText;

    AiGameBoard board = AiGameBoard.getInstance();
    Game game;
    Stack<Checker>[] points = board.getPoint();
    Shape[] polygon;
    ImageView[] guiDice;
    Map<Integer, Shape> pointMap;
    Map<Shape, Integer> shapeIndexMap;
    public Map<Circle, Checker> checkerMap;
    public Map<Checker, Circle> circleMap;
    Boolean aiPlayer;

    Point2D dragAnchor;
    private double initX;
    private double initY;
    private double stagex;
    private double stagey;

    public void init(Stage stage, Parent root) {
        ap = (AnchorPane) root;
        addDragListeners(menuBar, stage);

        background.setImage(new Image("gui/images/background.jpg"));

        polygon = new Shape[]{stack0, stack1, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9, stack10,
                stack11, stack12, stack13, stack14, stack15, stack16, stack17, stack18, stack19, stack20, stack21,
                stack22, stack23, stack24, stack25, stack26, stack27};

        guiDice = new ImageView[]{die1, die2, die3, die4};

        pointMap = new HashMap<Integer, Shape>();
        shapeIndexMap = new HashMap<Shape, Integer>();

        for (int i = 0; i < 28; i++) {
            pointMap.put(new Integer(i), polygon[i]);
            shapeIndexMap.put(polygon[i], new Integer(i));
        }
        checkerMap = new HashMap<Circle, Checker>();
        circleMap = new HashMap<Checker, Circle>();

        MoveValidationMethods.setGameBoard(board);
        board.registerObserver(this);
        board.setUp();
        board.setState(board.getBlackState());
        board.nextPlayer();
//        board.nextPlayer();
        enableCheckers(board.getState().getColor());
        game = new Game(board);
        game.registerObserver(this);
        board.setController(this);
    }

    public void drawChecker(Checker checker, int color) {
        Circle c = createChecker(color);
        checkerMap.put(c, checker);
        circleMap.put(checker, c);
        placeOnStack(c, checker.getPosition());
    }

    public Circle createChecker(final int color) {

        final Color checkerColor = (color == Constant.BLACK) ? Color.BLACK : Color.RED;
        final Circle checker = new Circle(20, checkerColor);

        checker.setTranslateX(50);
        checker.setTranslateY(50);

        checker.setFill(new RadialGradient(0, 0, 0.05, 0.1, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0, Color.rgb(250, 250, 255)), new Stop(1, checkerColor)}));

        checker.setCursor(Cursor.CLOSED_HAND);

        checker.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                checker.setEffect(new InnerShadow(7, checkerColor.brighter().brighter()));

            }
        });

        checker.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                checker.setEffect(null);

            }

        });

        checker.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && !event.isSecondaryButtonDown()) {
                    initX = checker.getTranslateX();
                    initY = checker.getTranslateY();
                    dragAnchor = new Point2D(event.getSceneX(), event.getSceneY());
                    highlightValidPoints(checkerMap.get(checker));
                    event.consume();
                } else
                    event.consume();
            }
        });

        checker.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                if (event.getButton() != MouseButton.PRIMARY | event.isSecondaryButtonDown()) {
                    event.consume();
                } else {
                    double dragX = event.getSceneX() - dragAnchor.getX();
                    double dragY = event.getSceneY() - dragAnchor.getY();

                    double newXPosition = initX + dragX;
                    double newYPosition = initY + dragY;

                    checker.toFront();
                    if ((newXPosition >= checker.getRadius()) && (newXPosition <= (ap.getWidth() - checker.getRadius()))) {
                        checker.setTranslateX(newXPosition);
                    }
                    if ((newYPosition >= checker.getRadius() + 25)
                            && (newYPosition <= (ap.getHeight() - checker.getRadius()))) {
                        checker.setTranslateY(newYPosition);
                    }
                    event.consume();
                }
            }
        });

        checker.setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Boolean insideShape;
                    Boolean acceptedMove = false;
                    for (Shape s : polygon) {
                        insideShape = s.getBoundsInParent().contains(new Point2D(event.getSceneX(), event.getSceneY()));
                        if (insideShape) {
                            int startPos = checkerMap.get(checker).getPosition();
                            int targetPos = shapeIndexMap.get(s);
                            placeOnStack(checker, targetPos);
                            acceptedMove = game.move(new Move(startPos, targetPos));
                            enableCheckers(board.getState().getColor());
                            break;
                        }
                    }
                    if (!acceptedMove) {
                        checker.setTranslateX(initX);
                        checker.setTranslateY(initY);
                    }
                    event.consume();
                    countBearOff();
                    removeHighlight();
                } else
                    event.consume();
            }
        });

        ap.getChildren().add(checker);
        return checker;
    }

    public void drawMovedCheckersByAi(Circle checker, Move move){
        placeOnStack(checker, move.getToPosition());
        game.move(new Move(move.getFromPosition(), move.getToPosition()));
        enableCheckers(board.getState().getColor());
        countBearOff();
        removeHighlight();
    }

    private void highlightValidPoints(Checker checker) {
//        game.getBestMove();
        DropShadow ds = new DropShadow(10, new Color(1, 1, 1, 1));
        ds.setSpread(0.7);
        ds.setWidth(30);
        ds.setHeight(0);
        ds.setRadius(10);
        ColorInput black = new ColorInput();
        black.setHeight(226);
        black.setWidth(130);
        black.setPaint(Color.BLACK);
        ColorInput red = new ColorInput();
        red.setHeight(226);
        red.setWidth(130);

        List<Integer> stacksToHighlight = MoveValidationMethods.getValidMovesForChecker(checker, game.getDice());
        for (Integer i : stacksToHighlight) {
            if (i == Constant.BLACK) {
                polygon[i].setEffect(black);
                polygon[i].setOpacity(0.4);
            } else if (i == Constant.RED) {
                polygon[i].setEffect(red);
                polygon[i].setOpacity(0.4);
            } else
                polygon[i].setEffect(ds);
        }
    }

    private void removeHighlight() {
        for (Shape p : polygon) {
            p.setEffect(null);
        }
    }

    public void placeOnStack(Circle checker, int point) {
        checker.setDisable(false);
        checker.toFront();
        Shape targetShape = pointMap.get(point);
        double pointX = targetShape.getLayoutX();
        double pointY = targetShape.getLayoutY() + 3;

        int checkersInStack = points[point].size();

        if (checkersInStack > 0) {
            Checker topchecker = (Checker) points[point].peek();
            circleMap.get(topchecker).setDisable(true);
        }

        boolean lowerBoard = point >= Constant.BLACK && point < 13 || point == Constant.REDBAR;

        if (lowerBoard) {
            if (point == Constant.REDBAR) {
                pointX += 35;
                pointY += 25 + (1.5 * checker.getRadius() * checkersInStack);
            } else if (point == Constant.BLACK) {
                pointX += 100;
                pointY += 200;
            } else
                pointY += 4 * checker.getRadius() - (1.5 * checker.getRadius() * checkersInStack);
        } else {
            if (point == Constant.BLACKBAR) {
                pointX += 35;
                pointY += +205 - (1.5 * checker.getRadius() * checkersInStack);
            } else if (point == Constant.RED) {
                pointX += 100;
                pointY += 25;
            } else {
                pointY += -5 * checker.getRadius() + (1.5 * checker.getRadius() * checkersInStack);
            }
        }

        checker.setTranslateX(pointX);
        checker.setTranslateY(pointY);

    }

    public void moveChecker(Checker checker, int toPoint) {
        Circle c = circleMap.get(checker);
        placeOnStack(c, toPoint);
    }

    @FXML
    public void rollDice() {
        game.roll();
        btnRoll.setDisable(true);
    }

    @FXML
    public void setAiPlayerFalse() {
        aiPlayer = false;
    }

    @FXML
    public void setAiPlayerTrue() {
        aiPlayer = true;
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void newGame() {
        for (Entry<Circle, Checker> entry : checkerMap.entrySet()) {
            ap.getChildren().remove(entry.getKey());
        }
        checkerMap.clear();
        circleMap.clear();
        board.setUp();
        board.nextPlayer();
        redBorneOffCount.setText(null);
        blackBorneOffCount.setText(null);
        informationText.setText(null);
        enableCheckers(board.getState().getColor());
        game.removeDice();
    }

    public void drawDice(ArrayList<Die> dice) {
        if (dice.isEmpty()) {
            btnRoll.setDisable(false);
        }
        for (ImageView i : guiDice) {
            i.setImage(null);
        }
        for (int i = 0; i < dice.size(); i++) {
            int dots = dice.get(i).getValue();
            guiDice[i].setImage(setDie(dots));
        }
    }

    public Image setDie(int dots) {
        String fileName = String.format("gui/images/Dice%d.png", dots);
        Image die = new Image(fileName);
        return die;
    }

    public void updatePlayer(int player) {
        if (player == Constant.RED) {
            goRed.setVisible(true);
            goBlack.setVisible(false);
        } else {
            goRed.setVisible(false);
            goBlack.setVisible(true);
        }
    }

    private void enableCheckers(int player) {
        disableAllCheckers();
        if (board.getState() == board.getBlackBarState() && !points[Constant.BLACKBAR].empty()) {
            Checker c = (Checker) points[Constant.BLACKBAR].peek();
            circleMap.get(c).setDisable(false);
        } else if (board.getState() == board.getRedBarState() && !points[Constant.REDBAR].empty()) {
            Checker c = (Checker) points[Constant.REDBAR].peek();
            circleMap.get(c).setDisable(false);
        } else {
            for (int i = 0; i < points.length; i++) {
                if (!points[i].empty()) {
                    Checker c = (Checker) points[i].peek();
                    if (c.color == player && c.getPosition() != Constant.BLACK && c.getPosition() != Constant.RED) {
                        circleMap.get(c).setDisable(false);
                    }
                }
            }
        }
    }

    private void disableAllCheckers() {
        for (Entry<Checker, Circle> entry : circleMap.entrySet()) {
            entry.getValue().setDisable(true);
        }
    }

    private void addDragListeners(final Node node, final Stage primaryStage) {

        node.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                stagex = node.getScene().getWindow().getX() - event.getScreenX();
                stagey = node.getScene().getWindow().getY() - event.getScreenY();
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + stagex);
                primaryStage.setY(event.getScreenY() + stagey);
            }
        });
    }

    public void countBearOff() {
        if (board.getState() == board.getBlackBearOffState() || board.getState() == board.getRedBearOffState()) {
            String blackCount = points[Constant.BLACK].empty() ? null : String.format("%d", points[Constant.BLACK].size());
            blackBorneOffCount.setText(blackCount);
            blackBorneOffCount.toFront();
            String redCount = points[Constant.RED].empty() ? null : String.format("%d", points[Constant.RED].size());
            redBorneOffCount.setText(redCount);
            redBorneOffCount.toFront();
        }
    }

    public Game getGame(){
        return game;
    }

    public Circle getCircle(Checker checker){
        return circleMap.get(checker);
    }

    public Checker getChecker(int position){
        for (Map.Entry<Circle, Checker> entry : checkerMap.entrySet()) {
            Checker checker = entry.getValue();
            Stack<Checker> stack = points[position];
            if (checker.getPosition() == position ){ //&& stack.get(stack.size()-1).equals(checker))
                return checker;
            }
        }
        return null;
    }


    public void notifyNoMoves() {
        informationText.setText("No possible moves");
        disableAllCheckers();
        btnRoll.setVisible(false);
        btnRoll.setDisable(true);
        btnOK.setVisible(true);
        btnOK.setDisable(false);
        btnOK.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                game.removeDice();
                btnOK.setVisible(false);
                btnOK.setDisable(true);
                btnRoll.setVisible(true);
                btnRoll.setDisable(false);
                informationText.setText(null);
                enableCheckers(board.getState().getColor());
            }
        });
    }

    public void notifyWinner(int player) {
        btnRoll.setDisable(true);
        String winner = (player == Constant.RED) ? "The Red player won" : "The Black player won";
        informationText.setText(winner);
    }
}