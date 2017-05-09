package cryogen;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * @author Elnette Moller
 * E-Mail: elnette.moller@gmail.com
 * Java class handler for the Cryptogen application main GUI
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */
public class Cryptogen implements Initializable
{
    //Instance Variables
    private List<File> files; //List of files to be encrypted or decrypted
    private boolean exiting;
    //GUI Instance Variables
    @FXML private TitledPane pneAlgorithmsPane;
    @FXML private StackPane stackPane;
    @FXML private TitledPane pneFilePane;
    @FXML private RadioButton radVigenere;
    @FXML private RadioButton radVernam;
    @FXML private RadioButton radColumnarTrans;
    @FXML private RadioButton radElephant;
    @FXML private TextArea txtMessage;
    @FXML private TextArea txtKey;
    private final ToggleGroup algorithms;
    private Stage currentStage;

    //Default Constructor
    public Cryptogen()
    {
        files = null;
        exiting = false;
        algorithms = new ToggleGroup();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void initialize(Stage currentStage)
    {
        this.currentStage = currentStage;
        getCurrentStage().setOnCloseRequest(confirmCloseEventHandler);//Set default close event
        radVigenere.setToggleGroup(algorithms);
        radVigenere.setSelected(true);
        radVernam.setToggleGroup(algorithms);
        radColumnarTrans.setToggleGroup(algorithms);
        radElephant.setToggleGroup(algorithms);
        pneAlgorithmsPane.requestFocus();
    }

    public Stage getCurrentStage()
    {
        return this.currentStage;
    }


    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnEncryptMessage_Clicked(ActionEvent event) throws IOException
    {
        try
        {
            /*plainPassword = pswPassword.getText().toCharArray();
            if (new String(plainPassword).equals(""))
            {
                pswPassword.requestFocus();
                throw new Exception("Please Enter a Password");
            }
            key = pswKey.getText().toCharArray();
            if (new String(key).equals(""))
            {
                pswKey.requestFocus();
                throw new Exception("Please Enter a Key");
            }

            int limit;
            if(cbxCompact.isSelected())
                limit = 12;
            else
                limit = 32;

            cipherPassword = encrypt(plainPassword, key, limit);
            if(cipherPassword == null)
                throw new Exception("Error Occurred During Encryption");

            Stage passWindow = new Stage(StageStyle.TRANSPARENT);
            passWindow.getIcons().add(new Image(getClass().getResourceAsStream("/cryogen/icon.png")));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Output.fxml"));
            passWindow.setHeight(186);
            passWindow.setWidth(495);
            passWindow.setResizable(false);
            passWindow.setScene(new Scene((Pane)loader.load(), Color.TRANSPARENT));
            Output pass = loader.<Output>getController();
            pass.initialize(cipherPassword);
            passWindow.showAndWait();*/
        }
        catch (Exception ex)
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText(ex.getMessage());
            error.showAndWait();
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnEncryptFiles_Clicked(ActionEvent event) throws IOException//TODO:Add dialog with progress bar
    {//TODO:Create Exception classes
        try
        {
            if(txtKey.getText() == "")
            {
                throw new InputMismatchException("Please Enter a Key");//TODO:Highlight text area
            }
            if(files.isEmpty())
            {
                throw new FileSystemNotFoundException("Please drag some files onto the highlighted area for encryption.");//TODO:Highlight Drag and Drop area
            }
            if(radVigenere.isSelected())
            {
                char[] key = txtKey.getText().toCharArray();
                for (int ii = 0; ii < files.size(); ii++)//Encrypt Each File
                {
                    //File plainFile = new File(String.valueOf(new FileInputStream(files.get(ii).getAbsolutePath())));
                    Path path = Paths.get(files.get(ii).getAbsolutePath());
                    byte[] plainFileData = Files.readAllBytes(path);
                    byte[] cipherFileData = Cryptography.VigenereCipher.encrypt(plainFileData, key);

                    FileOutputStream fos = new FileOutputStream(files.get(ii).getAbsoluteFile() + ".cg");
                    fos.write(cipherFileData);
                    fos.close();
                    System.out.println(ii + ": " + files.get(ii).getAbsolutePath());
                }
            }
            else if(radVernam.isSelected())
            {

            }
            else if(radColumnarTrans.isSelected())
            {

            }
            else if (radElephant.isSelected())
            {

            }
            else
                throw new InputMismatchException("Please Choose an Algorithm for Encryption/Decryption");
        }
        catch (InputMismatchException ex)
        {
            handleException(ex, "Error", "Choose Algorithm", ex.getMessage());
        }
        catch (FileSystemNotFoundException ex)
        {
            handleException(ex, "Error", "Drag and Drop Files", ex.getMessage());
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnDecryptMessage_Clicked(ActionEvent event) throws IOException
    {
        try
        {

        }
        catch (Exception ex)
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText(ex.getMessage());
            error.showAndWait();
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnDecryptFiles_Clicked(ActionEvent event) throws IOException//TODO: Add dialog with progress bar
    {
        try
        {
            if(radVigenere.isSelected())
            {
                char[] key = txtKey.getText().toCharArray();
                for(int v = 0; v < files.size(); v++)//Decrypt Each File
                {
                    Path path = Paths.get(files.get(v).getAbsolutePath());
                    byte[] cipherFileData = Files.readAllBytes(path);
                    byte[] plainFileData = Cryptography.VigenereCipher.decrypt(cipherFileData, key);

                    FileOutputStream fos = new FileOutputStream(files.get(v).getAbsolutePath().substring(0, files.get(v).getAbsolutePath().length() - 3));
                    fos.write(plainFileData);
                    fos.close();
                }
            }
            else if(radVernam.isSelected())
            {

            }
            else if(radColumnarTrans.isSelected())
            {

            }
            else if (radElephant.isSelected())
            {

            }
            else
                throw new InputMismatchException("Please Choose an Algorithm for Encryption/Decryption");
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     * Event necessary to execute before DragDropped may be executed, allows DragDropped to receive files by Copy or Move
     * @param event
     */
    @FXML
    protected void onDragOver(DragEvent event)
    {
        //data is dragged over the target
        //accept it only if it is not dragged from the same node
        //and if it has a string data
        if(event.getGestureSource() != stackPane && event.getDragboard().hasString())
        {
            //allow for both copying and moving, whatever user chooses
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    /**
     * Do something when an Object is dragged over the StackPane
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onDragEntered(DragEvent event)
    {
        pneFilePane.getStyleClass().remove("pneDefault");
        pneFilePane.getStyleClass().add("pneFilePaneDrag");
    }

    /**
     * Do something when an object exits the StackPane
     * @param event
     */
    @FXML
    protected void onDragExited(DragEvent event)
    {
        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
        pneFilePane.getStyleClass().add("pneDefault");
    }

    /**
     * Do something when an object is dropped onto the StackPane
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onDragDropped(final DragEvent event) throws IOException
    {
        final Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles())
        {
            success = true;
            // Only get the first file from the list
            files = db.getFiles();
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        for(int i = 0; i < files.size(); i++)
                            System.out.println(files.get(i).getAbsolutePath());

                        if(!stackPane.getChildren().isEmpty())
                        {
                            stackPane.getChildren().remove(0);
                        }
                        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
                        pneFilePane.getStyleClass().remove("pneDefault");
                        pneFilePane.getStyleClass().add("pneFilePaneDropped");
                        System.out.println("Drop Successful!");
                    }
                    catch (Exception ex)
                    {
                        //Logger.getLogger(Cryptogen.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(ex.toString());
                    }
                }
            });
        }
        event.setDropCompleted(success);
        event.consume();
    }

    //TODO: Remove this method after Vigenère's Cipher has been implemented in Cryptography class
    /**
     * Method to encrypt the password
     * Based on Vigenere's Cipher Algorithm, modified by Zander
     * @param newMessage the password to be encrypted
     * @param key          the key used to encrypt the password
     * @return the encrypted password
     */
    public static char[] encrypt(char[] newMessage, char[] key, int limit)
    {
        try
        {
            char[] systemMessage = new char[newMessage.length + 1];
            char[] finalMessage = new char[newMessage.length * 2 + 1];
            int keyIndex = 0;
            int i = 0;
            int ii = 0;
            int temp;
            int specCharCount = 0;
            int pos = 0;
            char[] specChars = new char[newMessage.length + 1];

            for (char t : newMessage)
            {
                if (t >= 65 && t <= 90)//Encrypting Uppercase Characters
                {
                    temp = t - 65 + (key[keyIndex] - 65);
                    if (temp < 0)
                        temp += 26;
                    if (temp <= 0)
                        temp += 26;

                    systemMessage[i++] = (char) (65 + (temp % 26));
                    if (++keyIndex == key.length)
                        keyIndex = 0;
                }
                else if (t >= 97 && t <= 122)//Encrypting Lower Case Characters
                {
                    temp = t - 97 + (key[keyIndex] - 97);
                    if (temp < 0)
                        temp += 26;
                    if (temp < 0)
                        temp += 26;

                    systemMessage[i++] = (char) (97 + (temp % 26));
                    if (++keyIndex == key.length)
                        keyIndex = 0;
                }
                else//Encrypting Special Characters
                {
                    specChars[ii++] = (char) (pos + 65);
                    specChars[ii++] = t;
                    specCharCount++;
                }
                pos++;
            }
            i = 0;
            finalMessage[i++] = (char) (specCharCount == 0 ? 65 : (--specCharCount + 65));//Encrypting Amount of Special Characters in Password
            for (char t = specChars[0]; t != 0; i++, t = specChars[i - 1])//Encrypting Special Characters & Positions of Special Characters
                finalMessage[i] = t;
            ii = i;
            for (char t = systemMessage[0]; t != 0; i++, t = systemMessage[i - ii])//Encrypting Password
                finalMessage[i] = t;

            int ext = -1;
            if(i > 32)
            {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Warning");
                confirm.setHeaderText( "Password is greater than 32 characters");
                confirm.setContentText("Would you like to shorten the password to the 32 limit?");
                confirm.getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
                Optional<ButtonType> result = confirm.showAndWait();
                if(result.isPresent() ? result.get() == ButtonType.YES : null) //Warning as ek nie toets met isPresent() nie want result.get() is optional of nullable
                    ext = 1;
            }

            int length = 0;
            for(int x = 0; finalMessage[x] != '\0'; x++)
                length++;
            char[] cipherMessage = new char[length];
            for(int xi = 0; xi < cipherMessage.length && xi < length; xi++)
                cipherMessage[xi] = finalMessage[xi];

            //Shuffle Password
            LinkedList<Character> evens = new LinkedList<>();
            LinkedList<Character>odds = new LinkedList<>();
            for(int iii = 0; iii < cipherMessage.length; iii++)
                if((int)cipherMessage[iii] % 2 == 0)
                    evens.addLast(cipherMessage[iii]);
                else
                    odds.addFirst(cipherMessage[iii]);
            int iv = 0;
            while(!evens.isEmpty() || !odds.isEmpty())
            {
                if (!odds.isEmpty())
                {
                    cipherMessage[iv++] = odds.getFirst();
                    odds.removeFirst();
                }
                if(!evens.isEmpty())
                {
                    cipherMessage[iv++] = evens.getFirst();
                    evens.removeFirst();
                }
            }

            //encrypt special chars further
            for(int v = 0; v < cipherMessage.length; v++)
                if((int)cipherMessage[v] <= 47)
                    cipherMessage[v] += 10;
                else if((int)cipherMessage[v] > 47 && (int)cipherMessage[v] < 64)
                    cipherMessage[v] -= 5;
                else if((int)cipherMessage[v] > 90 && (int)cipherMessage[v] <= 96)
                    if(cipherMessage.length % 2 == 0)
                        cipherMessage[v] += 2;
                    else
                        cipherMessage[v] -= 2;

            //Replacing unloved characters
            for(int vi = 0; vi < cipherMessage.length; vi++)
                if((int)cipherMessage[vi] == 34)
                    cipherMessage[vi] = 123;
                else if((int)cipherMessage[vi] == 38)
                    cipherMessage[vi] = 124;
                else if((int)cipherMessage[vi] == 60)
                    cipherMessage[vi] = 125;
                else if((int)cipherMessage[vi] == 62)
                    cipherMessage[vi] = 126;

            //Limitations
            if(ext == 1 || limit < 32)
            {
                char[] cipherMessageLimited = new char[limit < cipherMessage.length ? limit : cipherMessage.length];
                for (int vii = 0; vii < cipherMessage.length && vii < limit; vii++)
                    cipherMessageLimited[vii] = cipherMessage[vii];
                return cipherMessageLimited;
            }

            return cipherMessage;
        }
        catch (Exception ex)
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Failed to Encrypt Message");
            error.setHeaderText(null);
            error.setContentText(ex.getMessage());
            error.showAndWait();

            return null;
        }
    }

    /**
     * Event handler for File -> Clear Files
     * Used to clear loaded files
     * @param event
     */
    @FXML protected void mnuFile_ClearFiles_Clicked(ActionEvent event)
    {
        files = null;
        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
        pneFilePane.getStyleClass().remove("pneFilePaneDropped");
        pneFilePane.getStyleClass().add("pneFilePane");
    }

    /**
     * Event handler method for File -> Exit
     * @param event
     */
    @FXML protected void mnuFile_Exit_Clicked(ActionEvent event)
    {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(getCurrentStage());
        exiting = true;
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get()))
            System.exit(0);
        else
            exiting = false;
    }

    /**
     * Method to prompt before exit
     * Exits application with 0 error code if user prompt is confirmed else application continues
     */
    private EventHandler<WindowEvent> confirmCloseEventHandler = event ->
    {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(getCurrentStage());
        exiting = true;
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get()))
        {
            exiting = false;
            event.consume();
        }
    };

    protected void handleException(Exception ex)
    {
        handleException(ex, "Error");
    }

    protected void handleException(Exception ex, String title)
    {
        handleException(ex, title, null);
    }

    protected void handleException(Exception ex, String title, String header)
    {
        handleException(ex, title, header, ex.getMessage());//TODO:Check ex.toString() vs ex.getMessahe()
    }

    protected void handleException(Exception ex, String title, String header, String content)
    {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setHeaderText(header);
        error.setContentText(content);
        error.showAndWait();
    }
}
