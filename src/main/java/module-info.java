module com.sample.shoppingcart {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires javafx.media;

    opens com.sample.shoppingcart to javafx.fxml;
    exports com.sample.shoppingcart;
}