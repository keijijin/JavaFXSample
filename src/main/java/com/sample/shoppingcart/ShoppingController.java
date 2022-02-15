package com.sample.shoppingcart;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ShoppingController implements Initializable {
    @FXML
    private Label unitLabel, priceLabel, quantityLabel, totalLabel;
    @FXML
    private Slider quantitySlider;
    @FXML
    private ComboBox<Item> itemComboBox;

    @FXML
    private Button cartInButton, cartOutButton;

    @FXML
    private TableColumn itemNameCol, quantityCol, priceCol;

    @FXML
    private TableView tableView;

    @FXML
    private ImageView imageView;

    @FXML
    private MediaView mediaView;

    ObservableList<CartItem> cartList;
    ObservableList<Item> itemsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData("src/main/resources/com/sample/shoppingcart/goods.csv");
        itemComboBox.getItems().addAll(itemsObservableList);

        cartList = FXCollections.observableArrayList();

        itemNameCol.setCellValueFactory(new PropertyValueFactory("itemName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory("price"));

        ObjectBinding<Item> itemBinding = new ObjectBinding<>() {
            {
                super.bind(itemComboBox.valueProperty());
            }

            @Override
            protected Item computeValue() {
                if (itemComboBox.getSelectionModel().getSelectedIndex() >= 0) {
                    return itemComboBox.getSelectionModel().getSelectedItem();
                }
                return new Item();
            }
        };

        unitLabel.textProperty().bind(Bindings.format("%d %s",
                Bindings.select(itemBinding, "unitQuantity"),
                Bindings.select(itemBinding, "unit")));
        priceLabel.textProperty().bind(Bindings.format("¥ %s",
                Bindings.select(itemBinding, "price")));
    }

    private void loadData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Item item = new Item(values[0], values[1], Integer.parseInt(values[2]), new BigDecimal(values[3]), values[4]);
                itemsObservableList.add(item);
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

        quantitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                quantityLabel.setText(String.format("%.0f", quantitySlider.getValue()));
                if(itemComboBox.getSelectionModel().isEmpty()) {
                    quantitySlider.setValue(0);
                }
            }
        });
    }

    @FXML
    public void pickGoods(ActionEvent event) {
        Item item = itemComboBox.getSelectionModel().getSelectedItem();
        if (item != null) {
            //unitLabel.setText(String.format("%d %s", item.getUnitQuantity(), item.getUnit()));
            //priceLabel.setText(String.format("¥ %s", item.getPrice().toString()));
            quantitySlider.setValue(0);
            System.out.println(item.getPicture());
            if(!item.getPicture().isEmpty()) {
                String exte = item.getPicture().substring(item.getPicture().length()-3, item.getPicture().length());
                System.out.println(exte);
                if (exte.equals("jpg")) {
                    File file = new File("src/main/resources/com/sample/shoppingcart/" + item.getPicture());
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } else if (exte.equals("mp4")) {
                    File file = new File("src/main/resources/com/sample/shoppingcart/" + item.getPicture());
                    Media media = new Media((file.toURI().toString()));
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaView = new MediaView(mediaPlayer);
                    mediaPlayer.play();
                }
            }

        }
    }

    public void carIn(ActionEvent event) {
        Item item = itemComboBox.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(quantityLabel.getText());
        if(quantity == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("How many goods do you want?");
            alert.setContentText("Change Quantity Slider!");
            alert.showAndWait();
        }
        if(item != null && quantity > 0) {
            CartItem cartItem = new CartItem(item.getName(), quantity, item.getPrice().multiply(new BigDecimal(quantity)));
            cartList.add(cartItem);

            System.out.println(cartItem.getItemName() + " : " + cartItem.getQuantity() + " : " + cartItem.getPrice());

            setCartItems();
        }
    }

    private void setCartItems() {
        tableView.setItems(cartList);
        totalLabel.setText(CalcTotal(cartList));
    }

    private String CalcTotal(ObservableList<CartItem> cartList) {
        BigDecimal total = new BigDecimal(0);
        for(CartItem item : cartList) {
            total = total.add(item.getPrice());
        }
        return total.toString();
    }

    public void cartOut(ActionEvent event) {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Select Cart Item first!");
            alert.setHeaderText("Select Item");
            alert.setContentText("You shoud select item in cart!!");
            alert.show();
        } else {
            cartList.remove(index);
            setCartItems();
            clearAllSelection();
        }
    }

    private void clearAllSelection() {
        tableView.getSelectionModel().clearSelection();
        itemComboBox.getSelectionModel().clearSelection();
        quantitySlider.setValue(0);
    }

    public void changeNumbers(MouseEvent mouseEvent) {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            String itemName = cartList.get(index).getItemName();
            for (int i = 0; i < itemsObservableList.size(); i++) {
                if (itemsObservableList.get(i).getName().equals(itemName)) {
                    itemComboBox.getSelectionModel().select(i);
                }
            }
            quantitySlider.setValue(cartList.get(index).getQuantity());
        }
    }
}