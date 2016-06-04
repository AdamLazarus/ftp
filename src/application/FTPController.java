package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class FTPController {
	@FXML TextField serverField;
	@FXML TextField portField;
	@FXML TextField userField;
	@FXML TextField passField;
	@FXML ListView<String> ftpItems;
	@FXML ListView<String> userItems;
	@FXML CheckBox hiddenItemsUser;
	@FXML CheckBox hiddenItemsFTP;
	FTPDownload downloader;


	 @FXML protected void handleConnectButtonAction(ActionEvent event) {
		 downloader = new FTPDownload(serverField.getText(), Integer.parseInt(portField.getText()), userField.getText(), passField.getText());
		 ftpItems.setItems(downloader.getFTPItems(hiddenItemsFTP.isSelected()));
		 userItems.setItems(downloader.getUserItems(hiddenItemsUser.isSelected()));
		 //downloader.Download();
	    }

	 @FXML protected void handleServerListCellDoubleClick(MouseEvent event){
		 if (event.getClickCount() == 2) {
			 String selectedItem = ftpItems.getSelectionModel().getSelectedItem();
			 if(selectedItem.startsWith("[") && selectedItem.endsWith("]")){
				 selectedItem = selectedItem.substring(1);
				 selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
				 downloader.changeDirectory(selectedItem);
				 ftpItems.setItems(downloader.getFTPItems(hiddenItemsFTP.isSelected()));
			 }
			 else{
				 downloader.Download(selectedItem);
			 }
		 }
	 	}

	 @FXML protected void handleUserListCellDoubleClick(MouseEvent event){
		 if (event.getClickCount() == 2) {
			 String selectedItem = userItems.getSelectionModel().getSelectedItem();
			 if(selectedItem.startsWith("[") && selectedItem.endsWith("]")){
				 selectedItem = selectedItem.substring(1);
				 selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
				 downloader.changeUserDirectory(selectedItem);
				 userItems.setItems(downloader.getUserItems(hiddenItemsUser.isSelected()));
			 }
			 else{
				 downloader.Upload(selectedItem);
			 }
		 }
	 	}
	 }
