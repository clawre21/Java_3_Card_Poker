import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
//import java.lang.Class<T>;

import static javafx.application.Application.launch;

// use relative path?
public class ThreeCardPokerGame extends Application{
    Player playerOne = new Player();
    Player playerTwo = new Player();
    Dealer theDealer = new Dealer();
    HBox player1Cards = new HBox();
    HBox player2Cards =  new HBox();
    HBox dealerCards =  new HBox();

    PauseTransition pauseNewTie = new PauseTransition(Duration.seconds(5));

    boolean newLookBool = false;

    private Button startButton;


    private Button b2;


    int winner_1 = 1;
    int winner_2 = 1;

    private TextField t1;
    private MenuBar menu;

    private TextField t2;
    HashMap<String, Scene> sceneMap;
    private BorderPane border1;


    private EventHandler<ActionEvent> myHandler;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Three Card Poker ");
        t1 = new TextField();
        startButton = new Button("start game");
        sceneMap = new HashMap<String,Scene>();


        startButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("bets")));

        sceneMap.put("start", createStartScene());
        sceneMap.put("gameplay",  createGamePlayScene(primaryStage));

        sceneMap.put("bets",   placeBetsScene(primaryStage));


        primaryStage.setScene(sceneMap.get("start"));
        primaryStage.show();

    }

    public void dealCards(){
        playerOne.hand =  theDealer.dealHand();
        playerTwo.hand = theDealer.dealHand();
        theDealer.dealersHand=theDealer.dealHand();
        player1Cards.getChildren().add(getCardPics(playerOne.hand));
        player2Cards.getChildren().add(getCardPics(playerTwo.hand));

        for (int i=0; i<3; i++ ){

            Image pic = new Image("back.jpeg");
            //Creating the image view
            ImageView imageView = new ImageView(pic);
            //Setting image to the image view
            imageView.setImage(pic);
            imageView.setX(10);
            imageView.setY(10);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
//            images.add(imageView);
            dealerCards.getChildren().add(imageView);

        }
    }

    public void resetBoard(){
        dealerCards.getChildren().clear();
        player1Cards.getChildren().clear();
        player2Cards.getChildren().clear();
        playerOne.hand.clear();
        playerTwo.hand.clear();
        theDealer.dealersHand.clear();
    }

    public void addDealersCard(){
        dealerCards.getChildren().add(getCardPics(theDealer.dealersHand));
    }
    public Scene createStartScene() {
        // making a border pane and setting margins, also color
        border1 = new BorderPane();
        BackgroundFill bg = new BackgroundFill(Color.GREEN, new CornerRadii(1), new Insets(0, 0, 0, 0));
        border1.setBackground(new Background(bg));
        t1.setText("Welcome to the Poker Game");
        t1.setAlignment(Pos.CENTER);
        startButton.setAlignment(Pos.CENTER);
        startButton.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));


        t1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
        t1.setEditable(false);

        // making the start button center
        VBox buttonBox = new VBox(startButton);
        buttonBox.setAlignment(Pos.CENTER);

        Image pic = new Image("start.jpeg");

        //Creating the image view
        ImageView imageView = new ImageView(pic);
        //Setting image to the image view
        imageView.setImage(pic);
        //Setting the image view parameters
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitHeight(300);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);
        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox root = new VBox(t1, buttonBox, imageBox); // creating a new Vbox
        root.setAlignment(Pos.CENTER);
        border1.setCenter(root);

        return new Scene( border1,850,750);
    }

    public int fold(Dealer dealer, Player thePlayer){
        ThreeCardLogic game = new ThreeCardLogic();
        int winner = game.compareHands(dealer.dealersHand, thePlayer.hand);
        // play bet is set to zero
        thePlayer.playBet = 0;
        thePlayer.totalWinnings = thePlayer.totalWinnings - thePlayer.anteBet - thePlayer.playBet - thePlayer.pairPlusBet;
        return winner;
    }
    public int decideWinner(Dealer dealer, Player thePlayer){
        ThreeCardLogic game = new ThreeCardLogic();
        int winner = game.compareHands(dealer.dealersHand, thePlayer.hand);
        int pair = game.evalPPWinnings(thePlayer.hand, thePlayer.pairPlusBet);
        if(thePlayer.pairPlusBet == pair){
            thePlayer.totalWinnings -= pair;
        }
        else{
            thePlayer.totalWinnings += pair;
        }
        if (winner == 0 | winner == -1){
            return winner;
        }
        else if(winner == 1){ // dealer wins
            thePlayer.totalWinnings = thePlayer.totalWinnings - thePlayer.anteBet - thePlayer.playBet;
        }
        else if(winner == 2){ // player wins
            thePlayer.totalWinnings = thePlayer.totalWinnings + 2*thePlayer.anteBet + 2*thePlayer.playBet;
        }
        return winner;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public Scene placeBetsScene(Stage primaryStage) {
        TextField errCheck = new TextField();


        errCheck.setText("Bets and Pairpluses must be numeric and cannot exceed $25");
        errCheck.setEditable(false);
        errCheck.setVisible(false);
        errCheck.setStyle("-fx-font-size:12;" + "-fx-font-color:red;" + "-fx-background-color: transparent");

        TextField errCheck2 = new TextField();
        errCheck2.setText("Bets and Pairpluses must be numeric and cannot exceed $25");
        errCheck2.setEditable(false);
        errCheck2.setVisible(false);
        errCheck2.setStyle("-fx-font-size:12;" + "-fx-font-color:red;" + "-fx-background-color: transparent");
        menu = new MenuBar(); //a menu bar takes menus as children
        Menu mOne = new Menu("Menu"); //a menu goes inside a menu bar

        MenuItem exit = new MenuItem("Exit"); //menu items go inside a menu
        MenuItem freshStart = new MenuItem("Fresh Start");
        MenuItem newLook = new MenuItem("New Look");

        //event handler for menu item
        exit.setOnAction(e->primaryStage.close());

        newLook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {

                newLookBool = !newLookBool;

                // call bet scene again

                sceneMap.put("bets",  placeBetsScene(primaryStage));
                primaryStage.setScene(sceneMap.get("bets") );
                primaryStage.show();
            }
        });


        mOne.getItems().add(exit); //add menu item to first menu
        mOne.getItems().add(freshStart);
        mOne.getItems().add(newLook);

        menu.getMenus().addAll(mOne); //add two menu

        BorderPane border1 = new BorderPane();
        ListView<String> displayInfo;
        ObservableList<String> storeInfo;

        TextField p1AnteButton = new TextField(); // dealer label
        TextField p1PairPlusButton = new TextField();
        TextField p2AnteButton = new TextField(); // dealer label
        TextField p2PairPlusButton = new TextField();


        TextField t1 = new TextField(); // dealer label
        TextField t2 = new TextField(); // player 1 label
        TextField t3 = new TextField(); // player 2 label
        t1.setEditable(false);
        t2.setEditable(false);
        t3.setEditable(false);

        TextField p1 = new TextField(); // dealer label
        TextField p2 = new TextField(); // player 1 label

        TextField p1Ante = new TextField();
        TextField p2Ante = new TextField();

        p1Ante.setEditable(false);
        p2Ante.setEditable(false);

        TextField p1PairPlus = new TextField();
        TextField p2PairPlus = new TextField();

        p1PairPlus.setEditable(false);
        p2PairPlus.setEditable(false);

        TextField p1Winning = new TextField();
        TextField p2Winning = new TextField();

        p1Winning.setEditable(false);
        p2Winning.setEditable(false);
        displayInfo = new ListView();
        storeInfo = FXCollections.observableArrayList();


        displayInfo.setPrefHeight(75);

        t2.setText("PLAYER 1");
        p1Ante.setText("Ante Bet: " + String.valueOf(playerOne.anteBet));
        p1PairPlus.setText("Pair Plus Bet: " + String.valueOf(playerOne.pairPlusBet));
        p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
        VBox p1AllScores = new VBox(t2, p1Ante, p1PairPlus, p1Winning);

        t3.setText("PLAYER 2");
        p2Ante.setText("Ante Bet: " + String.valueOf(playerTwo.anteBet));
        p2PairPlus.setText("Pair Plus Bet: " + String.valueOf(playerTwo.pairPlusBet));
        p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
        VBox p2AllScores = new VBox(t3, p2Ante, p2PairPlus, p2Winning);



        Button b1 = new Button("Confirm Player1 Bet");
        Button b2 = new Button("Confirm Player2 Bet");

        b2.setDisable(true);

        if (newLookBool){
            BackgroundFill bg = new BackgroundFill(Color.DARKBLUE,
                    new CornerRadii(1), new Insets(0, 0, 0, 0));
            border1.setBackground(new Background(bg));
        }
        else{
            BackgroundFill bg = new BackgroundFill(Color.GREEN,
                    new CornerRadii(1), new Insets(0, 0, 0, 0));
            border1.setBackground(new Background(bg));
        }

        p2AnteButton.setPromptText("Enter antebet/playbet");
        p2PairPlusButton.setPromptText("Enter pairplus");

        p1AnteButton.setPromptText("Enter antebet/playbet");
        p1PairPlusButton.setPromptText("Enter pairplus");

        p2AnteButton.setDisable(true);
        p2PairPlusButton.setDisable(true);

        t1.setText("PLACE YOUR BETS BELOW");
        t1.setAlignment(Pos.CENTER);
        t1.setStyle("-fx-font-size: 32;" + "-fx-background-color: transparent");
        t1.setPrefWidth(5000);
        HBox dealer = new HBox(t1); // changed from 10
        dealer.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: white;");
        dealer.setAlignment(Pos.CENTER);
        dealer.setPrefHeight(200);

        VBox player1 = new VBox(100, p1AnteButton,p1PairPlusButton, b1); // changed from 100

        player1.setPrefHeight(350);
        player1.setPrefWidth(500);


        storeInfo.add("New Game Has Started!");
        storeInfo.add("Player One's Turn...");
        displayInfo.setItems(storeInfo);
        b1.setOnAction(new EventHandler<ActionEvent>() {
            int t=0;
            public void handle(ActionEvent a) {
                if (isNumeric(p1AnteButton.getText()) && Integer.parseInt(p1AnteButton.getText()) <= 25) {
                    playerOne.anteBet = Integer.valueOf(p1AnteButton.getText());
                    p1AnteButton.setDisable(true);
                    p1PairPlusButton.setDisable(true);

                    if (isNumeric(p1PairPlusButton.getText()) && Integer.parseInt(p1PairPlusButton.getText()) <= 25) {
                        storeInfo.clear();
                        storeInfo.add("New Game Has Started!");
                        storeInfo.add("Player Two's Turn...");
                        playerOne.pairPlusBet = Integer.valueOf(p1PairPlusButton.getText());
                        b1.setDisable(true);
                        p1PairPlusButton.setDisable(true);
                        p2AnteButton.setDisable(false);
                        b2.setDisable(false);
                        p2PairPlusButton.setDisable(false);
                        errCheck.setVisible(false);
                        t1.clear();

                    } else {
                        p1PairPlusButton.setDisable(false);
                        t1.clear();
                        t1.setText("Bets must be numeric and cannot exceed $25");
                    }

                } else {
                    p1AnteButton.setDisable(false);
                    t1.clear();
                    t1.setText("Bets must be numeric and cannot exceed $25");
                }




            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                if (isNumeric(p2AnteButton.getText()) && Integer.parseInt(p2AnteButton.getText()) <= 25) {
                    playerTwo.anteBet = Integer.valueOf(p2AnteButton.getText());
                    p2AnteButton.setDisable(true);
                    p2PairPlusButton.setDisable(true);

                    if (isNumeric(p2PairPlusButton.getText()) && Integer.parseInt(p2PairPlusButton.getText()) <= 25) {

                        playerTwo.pairPlusBet = Integer.valueOf(p2PairPlusButton.getText());
                        b2.setDisable(true);
                        p2PairPlusButton.setDisable(true);
                        sceneMap.put("gameplay",  createGamePlayScene(primaryStage));
                        primaryStage.setScene(sceneMap.get("gameplay") );
                        t1.clear();
                        p1PairPlusButton.setDisable(false);
                        p1AnteButton.setDisable(false);
                        p2PairPlusButton.setDisable(false);
                        p2AnteButton.setDisable(false);
                        b1.setDisable(false);
                        b2.setDisable(false);
                        t1.clear();
                        t1.setText("PLACE YOUR BETS BELOW");

                    } else {
                        p2PairPlusButton.setDisable(false);
                        t1.clear();

                        t1.setText("Bets must be numeric and cannot exceed $25");
                    }

                } else {
                    p2AnteButton.setDisable(false);
                    t1.clear();
                    t1.setText("Bets must be numeric and cannot exceed $25");
                }


            }
        });


        player1.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: blue;");


        VBox player2 = new VBox(100, p2AnteButton, p2PairPlusButton, b2);
        player2.setPrefHeight(350);
        player2.setPrefWidth(500);
        player2.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: blue;");


        HBox playerBox = new HBox(player1, player2);
        HBox playerLabels = new HBox(p1AllScores, p2AllScores, displayInfo);

        VBox Labels = new VBox(menu, playerLabels, dealer, playerBox);

        border1.setCenter(Labels);

        return  new Scene(border1,1000, 1000);

    }

    public Scene placePairScene(Stage primaryStage, String description) {

        TextField errCheck = new TextField();


        errCheck.setText("Bets and Pairpluses must be numeric and cannot exceed $25");
        errCheck.setEditable(false);
        errCheck.setVisible(false);
        errCheck.setStyle("-fx-font-size:12;" + "-fx-font-color:red;" + "-fx-background-color: transparent");

        TextField errCheck2 = new TextField();
        errCheck2.setText("Bets and Pairpluses must be numeric and cannot exceed $25");
        errCheck2.setEditable(false);
        errCheck2.setVisible(false);
        errCheck2.setStyle("-fx-font-size:12;" + "-fx-font-color:red;" + "-fx-background-color: transparent");

        menu = new MenuBar(); //a menu bar takes menus as children
        Menu mOne = new Menu("Menu"); //a menu goes inside a menu bar
//        Menu mTwo = new Menu("option 2");

        MenuItem exit = new MenuItem("Exit"); //menu items go inside a menu
        MenuItem freshStart = new MenuItem("Fresh Start");
        MenuItem newLook = new MenuItem("New Look");

        //event handler for menu item
        exit.setOnAction(e->primaryStage.close());

        freshStart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
//
                resetBoard();
                playerOne.totalWinnings = 0;
                playerTwo.totalWinnings = 0;
                playerOne.anteBet = 0;
                playerTwo.anteBet = 0;
                playerOne.playBet = 0;
                playerTwo.playBet = 0;
                playerOne.pairPlusBet = 0;
                playerTwo.pairPlusBet = 0;
                // call bet scene again
                primaryStage.setScene(sceneMap.get("bets"));
                primaryStage.show();
            }
        });

        newLook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
//                sceneMap.put("bets",   placeBetsScene(primaryStage));
                // clear images and cards

                newLookBool = !newLookBool;

                // call bet scene again

                sceneMap.put("pair",  placePairScene(primaryStage, description));
                primaryStage.setScene(sceneMap.get("pair") );
                primaryStage.show();
            }
        });

        mOne.getItems().add(exit); //add menu item to first menu
        mOne.getItems().add(freshStart);
        mOne.getItems().add(newLook);

        menu.getMenus().addAll(mOne); //add two menu

        BorderPane border1 = new BorderPane();
        ListView<String> displayInfo;
        ObservableList<String> storeInfo;


        TextField t1 = new TextField(); // dealer label
        TextField t2 = new TextField(); // player 1 label
        TextField t3 = new TextField(); // player 2 label

        TextField p1AnteButton = new TextField(); // dealer label
        TextField p1PairPlusButton = new TextField();
        TextField p2AnteButton = new TextField(); // dealer label
        TextField p2PairPlusButton = new TextField();


        t1.setEditable(false);
        t2.setEditable(false);
        t3.setEditable(false);

        TextField p1 = new TextField(); // dealer label
        TextField p2 = new TextField(); // player 1 label

        TextField p1Ante = new TextField();
        TextField p2Ante = new TextField();

        p1Ante.setEditable(false);
        p2Ante.setEditable(false);

        TextField p1PairPlus = new TextField();
        TextField p2PairPlus = new TextField();

        p1PairPlus.setEditable(false);
        p2PairPlus.setEditable(false);

        TextField p1Winning = new TextField();
        TextField p2Winning = new TextField();

        p1Winning.setEditable(false);
        p2Winning.setEditable(false);
        displayInfo = new ListView();
        storeInfo = FXCollections.observableArrayList();


        displayInfo.setPrefHeight(75);

        t2.setText("PLAYER 1");
        p1Ante.setText("Ante Bet: " + String.valueOf(playerOne.anteBet));
        p1PairPlus.setText("Pair Plus Bet: 0");
        p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
        VBox p1AllScores = new VBox(t2, p1Ante, p1PairPlus, p1Winning);

        t3.setText("PLAYER 2");
        p2Ante.setText("Ante Bet: " + String.valueOf(playerTwo.anteBet));
        p2PairPlus.setText("Pair Plus Bet: 0");
        p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
        VBox p2AllScores = new VBox(t3, p2Ante, p2PairPlus, p2Winning);


        Button b1 = new Button("Confirm Player1 Bet");
        Button b2 = new Button("Confirm Player2 Bet");

        b2.setDisable(true);

        if (newLookBool){
            BackgroundFill bg = new BackgroundFill(Color.DARKBLUE,
                    new CornerRadii(1), new Insets(0, 0, 0, 0));
            border1.setBackground(new Background(bg));
            t1.setStyle("-fx-text-fill: red;");
        }
        else{
            BackgroundFill bg = new BackgroundFill(Color.GREEN,
                    new CornerRadii(1), new Insets(0, 0, 0, 0));
            border1.setBackground(new Background(bg));
        }


        p2AnteButton.setText(String.valueOf(playerTwo.anteBet));
        p2PairPlusButton.setPromptText("Enter pairplus");

        p1AnteButton.setText(String.valueOf(playerOne.anteBet));
        p1PairPlusButton.setPromptText("Enter pairplus");

        p1AnteButton.setDisable(true);
        p2AnteButton.setDisable(true);
//        p2PairPlusButton.setDisable(true);

        t1.setText(description);
        t1.setAlignment(Pos.CENTER);
        t1.setStyle("-fx-font-size: 32;" + "-fx-background-color: transparent");
//        t1.setPrefHeight(1000);
        t1.setPrefWidth(5000);
        HBox dealer = new HBox(t1); // changed from 10
//        root.getChildren().addAll(msgLbl, sayHelloBtn);
        dealer.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: white;");
        dealer.setAlignment(Pos.CENTER);
        dealer.setPrefHeight(200);

        VBox player1 = new VBox(100,p1AnteButton,p1PairPlusButton, b1); // changed from 100

        player1.setPrefHeight(350);
        player1.setPrefWidth(500);



        storeInfo.clear();
        storeInfo.add("New Round Has Started!");
        storeInfo.add("Player One Enter Pair Plus Bet...");
        displayInfo.setItems(storeInfo);
        p2PairPlusButton.setDisable(true);
        b1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                if (isNumeric(p1PairPlusButton.getText()) && Integer.valueOf(p1PairPlusButton.getText())<= 25) {
                    storeInfo.clear();
                    storeInfo.add("Player Two Enter Pair Plus Bet...");
                    displayInfo.setItems(storeInfo);
                    playerOne.pairPlusBet = Integer.valueOf(p1PairPlusButton.getText());
                    p1AnteButton.setDisable(true);
                    p1PairPlusButton.setDisable(true);
                    p2PairPlusButton.setDisable(false);
                    b1.setDisable(true);
                    b2.setDisable(false);
                    b1.setText("Confirm Player1 Bet");
                    t1.clear();
                }
                else{
                    p1PairPlusButton.setDisable(false);
                    p2PairPlusButton.setDisable(true);
                    b1.setDisable(false);
                    t1.clear();
                    t1.setText("Bets must be numeric and cannot exceed $25");
                }

            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {

                if (isNumeric(p2PairPlusButton.getText()) && Integer.parseInt(p2PairPlusButton.getText()) <= 25) {
                    playerTwo.pairPlusBet = Integer.valueOf(p2PairPlusButton.getText());
                    b2.setDisable(true);
                    p2PairPlusButton.setDisable(true);
                    sceneMap.put("gameplay",  createGamePlayScene(primaryStage));
                    primaryStage.setScene(sceneMap.get("gameplay") );
                    t1.clear();
                    b1.setDisable(false);
                    p1PairPlusButton.clear();
                    p2PairPlusButton.clear();
                    p1PairPlusButton.setDisable(false);
                    p2PairPlusButton.setDisable(false);
                    b2.setDisable(false);
                    t1.clear();
                    t1.setText("PLACE YOUR BETS BELOW");

                } else {
//                        b1.setDisable(false);
                    p2PairPlusButton.setDisable(false);
                    t1.clear();
                    t1.setText("Bets must be numeric and cannot exceed $25");

                }

            }
        });

        player1.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: blue;");


        VBox player2 = new VBox(100,p2AnteButton, p2PairPlusButton, b2); // changed from 10
        player2.setPrefHeight(350);
        player2.setPrefWidth(500);
        player2.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: blue;");


        HBox playerBox = new HBox(player1, player2); // instead of GridPane I used this
        HBox playerLabels = new HBox(p1AllScores, p2AllScores, displayInfo);

        VBox Labels = new VBox(menu, playerLabels, dealer, playerBox);

        border1.setCenter(Labels);

        return  new Scene(border1,1000, 1000);

    }

    public Scene createGamePlayScene(Stage primaryStage) {
        Button dealButtn = new Button();
        Button nextRound = new Button("Go To Next Round");
        Button P1Play = new Button("Play Hand");
        Button P2Play = new Button("Play Hand");

        Button P1Fold = new Button("Fold");
        Button P2Fold = new Button("Fold");
        Button b1 = new Button();

        P1Fold.setPrefWidth(100);
        P2Fold.setPrefWidth(100);
        P1Play.setPrefWidth(100);
        P2Play.setPrefWidth(100);

        P1Fold.setDisable(true);
        P2Fold.setDisable(true);
        P1Fold.setVisible(false);
        P2Fold.setVisible(false);

        P1Play.setDisable(true);
        P2Play.setDisable(true);
        P1Play.setVisible(false);
        P2Play.setVisible(false);
        menu = new MenuBar(); //a menu bar takes menus as children
        Menu mOne = new Menu("Menu"); //a menu goes inside a menu bar

        MenuItem exit = new MenuItem("exit"); //menu items go inside a menu
        MenuItem freshStart = new MenuItem("Fresh Start");
        MenuItem newLook = new MenuItem("New Look");


        //event handler for menu item
        exit.setOnAction(e->primaryStage.close());
        freshStart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                // clear images and cards
                resetBoard();
                playerOne.totalWinnings = 0;
                playerTwo.totalWinnings = 0;
                playerOne.anteBet = 0;
                playerTwo.anteBet = 0;
                playerOne.playBet = 0;
                playerTwo.playBet = 0;
                playerOne.pairPlusBet = 0;
                playerTwo.pairPlusBet = 0;
                P1Play.setVisible(false);
                P2Play.setVisible(false);
                P1Play.setDisable(true);
                P2Play.setDisable(true);

                P1Fold.setVisible(false);
                P2Fold.setVisible(false);
                P1Fold.setDisable(true);
                P2Fold.setDisable(true);

                dealButtn.setDisable(false);

                // call bet scene again
                primaryStage.setScene(sceneMap.get("bets"));
                primaryStage.show();
            }
        });

        newLook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                // clear images and cards

                newLookBool = !newLookBool;

                // call bet scene again

                sceneMap.put("gameplay",  createGamePlayScene(primaryStage));
                primaryStage.setScene(sceneMap.get("gameplay") );
                primaryStage.show();
            }
        });

        mOne.getItems().add(exit); //add menu item to first menu
        mOne.getItems().add(freshStart);
        mOne.getItems().add(newLook);

        menu.getMenus().addAll(mOne); //add two menu

        BorderPane border1 = new BorderPane();

        TextField t1 = new TextField(); // dealer label
        TextField t2 = new TextField(); // player 1 label
        TextField t3 = new TextField(); // player 2 label
        t1.setEditable(false);
        t2.setEditable(false);
        t3.setEditable(false);


        TextField p1 = new TextField(); // dealer label
        TextField p2 = new TextField(); // player 1 label

        TextField p1Ante = new TextField();
        TextField p2Ante = new TextField();

        p1Ante.setEditable(false);
        p2Ante.setEditable(false);

        TextField p1PairPlus = new TextField();
        TextField p2PairPlus = new TextField();

        p1PairPlus.setEditable(false);
        p2PairPlus.setEditable(false);

        TextField p1Winning = new TextField();
        TextField p2Winning = new TextField();

        p1Winning.setEditable(false);
        p2Winning.setEditable(false);

        t1.setText("PLAYER 1");
        p1Ante.setText("Ante Bet: " + String.valueOf(playerOne.anteBet));
        p1PairPlus.setText("Pair Plus Bet: " + String.valueOf(playerOne.pairPlusBet));
        p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
        VBox p1AllScores = new VBox(t1, p1Ante, p1PairPlus, p1Winning);

        t3.setText("PLAYER 2");
        p2Ante.setText("Ante Bet: " + String.valueOf(playerTwo.anteBet));
        p2PairPlus.setText("Pair Plus Bet: " + String.valueOf(playerTwo.pairPlusBet));
        p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
        VBox p2AllScores = new VBox(t3, p2Ante, p2PairPlus, p2Winning);



        dealButtn.setText("Deal");

        ListView<String> displayInfo;
        ObservableList<String> storeInfo;
        displayInfo = new ListView();
        storeInfo = FXCollections.observableArrayList();
        displayInfo.setPrefHeight(75);

        storeInfo.clear();
        storeInfo.add("Click Deal to Deal Cards...");
        displayInfo.setItems(storeInfo);

        dealButtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {

                // call this somewhere else to show the actual cards
                storeInfo.clear();
                storeInfo.add("Player One: Choose to Play or Fold");
                displayInfo.setItems(storeInfo);
                P1Play.setVisible(true);
                P2Play.setVisible(true);
                P1Play.setDisable(false);

                P1Fold.setVisible(true);
                P2Fold.setVisible(true);
                P1Fold.setDisable(false);

                dealCards();
                dealButtn.setDisable(true);

            }
        });
        P1Play.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                storeInfo.clear();
                storeInfo.add("Player Two: Choose to Play or Fold");
                displayInfo.setItems(storeInfo);
                playerOne.playBet = playerOne.anteBet;
                P1Play.setDisable(true);
                P1Fold.setDisable(true); // disable fold option
                // now allow player 2 to choose
                P2Play.setDisable(false);
                P2Fold.setDisable(false);

                winner_1 = 1;
                winner_1 = decideWinner(theDealer, playerOne);
            }
        });
        nextRound.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                sceneMap.put("pair", placePairScene(primaryStage, "NEW ROUND, SET PAIR PLUS BET"));
                primaryStage.setScene(sceneMap.get("pair"));
                primaryStage.show();
                resetBoard();
            }
        });
        pauseNewTie.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                sceneMap.put("tie", placePairScene(primaryStage, "NO QUEEN HIGH FOR DEALER, SET PAIR PLUS BET"));
                primaryStage.setScene(sceneMap.get("tie"));
                nextRound.setDisable(false);
                primaryStage.show();
                resetBoard();
            }
        });
        P2Play.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                playerTwo.playBet = playerTwo.anteBet;
                P2Play.setDisable(true); // disable play option
                P2Fold.setDisable(true);
                dealerCards.getChildren().clear(); // clear the back side of the card
                addDealersCard(); // add actual hand images
                // pause here to show dealers card in case not queen high or better

                winner_2 = 1;
                winner_2 = decideWinner(theDealer, playerTwo);

                // update listView with Game outcomes
                storeInfo.clear();
                if(winner_1 == 1){
                    storeInfo.add("Dealer Beat Player One");
                    displayInfo.setItems(storeInfo);
                }
                if(winner_1 == 2){
                    storeInfo.add("Player One Beat Dealer");
                    displayInfo.setItems(storeInfo);
                }
                if(winner_1 == 0){
                    storeInfo.add("Player One and Dealer Tie");
                    displayInfo.setItems(storeInfo);
                }


                if(winner_2 == 1){
                    storeInfo.add("Dealer Beat Player Two");
                    displayInfo.setItems(storeInfo);
                }
                if(winner_2 == 2){
                    storeInfo.add("Player Two Beat Dealer");
                    displayInfo.setItems(storeInfo);
                }
                if(winner_2 == 0){
                    storeInfo.add("Player Two and Dealer Tie");
                    displayInfo.setItems(storeInfo);
                }


                if((winner_1 == -1) | (winner_2 == -1)){
                    storeInfo.clear();
                    storeInfo.add("Dealer Does Not Have Queen High or Better, Ante Bet Pushed to Next Round");
                    displayInfo.setItems(storeInfo);
                    nextRound.setDisable(true);
                    p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
                    p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
                    pauseNewTie.play();
                }
                else {
                    p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
                    p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
                }

            }
        });

        P1Fold.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                storeInfo.clear();
                storeInfo.add("Player Two: Choose to Play or Fold");
                displayInfo.setItems(storeInfo);
                P1Play.setDisable(true);
                P1Fold.setDisable(true); // disable fold option

                // now allow player 2 to choose
                P2Play.setDisable(false);
                P2Fold.setDisable(false);

                winner_1 = 1;
                winner_1 = fold(theDealer, playerOne);
            }
        });

        P2Fold.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                P2Play.setDisable(true); // disable play option
                P2Fold.setDisable(true);
                dealerCards.getChildren().clear(); // clear the back side of the card
                addDealersCard(); // add actual hand images

                winner_2 = 1;
                winner_2 = fold(theDealer, playerTwo);

                // update listView with Game outcomes
                storeInfo.clear();
                if(winner_1 == 1){
                    storeInfo.add("Dealer Beat Player One");
                    displayInfo.setItems(storeInfo);
                }
                else if(winner_1 == 2){
                    storeInfo.add("Player One Beat Dealer");
                    displayInfo.setItems(storeInfo);
                }
                else if(winner_1 == 0){
                    storeInfo.add("Player One and Dealer Tie");
                    displayInfo.setItems(storeInfo);
                }


                if(winner_2 == 1){
                    storeInfo.add("Dealer Beat Player Two");
                    displayInfo.setItems(storeInfo);
                }
                else if(winner_2 == 2){
                    storeInfo.add("Player Two Beat Dealer");
                    displayInfo.setItems(storeInfo);
                }
                else if(winner_2 == 0){
                    storeInfo.add("Player Two and Dealer Tie");
                    displayInfo.setItems(storeInfo);
                }




                // NO QUEEN HIGH
                if((winner_1 == -1) | (winner_2 == -1)){
                    storeInfo.clear();
                    storeInfo.add("Dealer Does Not Have Queen High or Better, Ante Bet Pushed to Next Round \n Moving to Next Round...");
                    displayInfo.setItems(storeInfo);
                    nextRound.setDisable(true);
                    p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
                    p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
                    pauseNewTie.play();
                }
                else {
                    p1Winning.setText("Total Winnings: " + String.valueOf(playerOne.totalWinnings));
                    p2Winning.setText("Total Winnings: " + String.valueOf(playerTwo.totalWinnings));
                }
            }
        });


        if (newLookBool){
            BackgroundFill bg = new BackgroundFill(Color.DARKBLUE,
                    new CornerRadii(1), new Insets(0, 0, 0, 0));
            border1.setBackground(new Background(bg));
        }
        else{
            BackgroundFill bg = new BackgroundFill(Color.GREEN,
                    new CornerRadii(1), new Insets(0, 0, 0, 0));
            border1.setBackground(new Background(bg));
        }



        HBox dealer = new HBox(dealerCards);
        dealer.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;"+
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: green;");

        dealer.setPrefHeight(200);
        dealer.setPrefWidth(500);
        dealer.setAlignment(Pos.CENTER);

        HBox Player1Choice = new HBox(P1Play, P1Fold);
        Player1Choice.setAlignment(Pos.CENTER);
        VBox player1 = new VBox(player1Cards, Player1Choice);
        player1.setPrefHeight(350);
        player1.setPrefWidth(500);
        player1.setAlignment(Pos.CENTER);
        player1.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: green;");


        HBox Player2Choice = new HBox(P2Play, P2Fold);
        VBox player2 = new VBox(player2Cards, Player2Choice);
        Player2Choice.setAlignment(Pos.CENTER);
        player2.setPrefHeight(350);
        player2.setPrefWidth(500);
        player2.setAlignment(Pos.CENTER);
        player2.setStyle("-fx-padding: 100;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: green;");




        HBox playerBox = new HBox(player1, player2);
        HBox playerLabels = new HBox(p1AllScores, p2AllScores, displayInfo);

        VBox Labels = new VBox(menu, playerLabels, dealer, playerBox, dealButtn, nextRound);
        border1.setCenter(Labels);

        return  new Scene(border1,1000, 1000);

    }

    public  HBox getCardPics(ArrayList<Card> temp) {
        HBox imagesBox = new HBox();

        for (int i=0; i<3; i++ ){

            Image pic = new Image(temp.get(i).toString()+".png");
            //Creating the image view
            ImageView imageView = new ImageView(pic);
            //Setting image to the image view
            imageView.setImage(pic);
            imageView.setX(10);
            imageView.setY(10);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imagesBox.getChildren().add(imageView);

        }
        return imagesBox;
    }


}