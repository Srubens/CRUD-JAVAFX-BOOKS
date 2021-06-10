/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package democrudbooks;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Ferreira
 */
public class MainController implements Initializable {
    
    @FXML
    private TextField tfid;
    
    @FXML
    private TextField tftitle;
    
    @FXML
    private TextField tfautor;
    
    @FXML
    private TextField tfyear;
    
    @FXML
    private TextField tfpages;
    
    @FXML
    private TableView<Books> tvBooks;
    
    @FXML
    private TableColumn<Books, Integer> colid;
    
    @FXML
    private TableColumn<Books, String> coltitle;
    
    @FXML
    private TableColumn<Books, String> colauthor;
    
    @FXML
    private TableColumn<Books, Integer> colyear;
    
    @FXML
    private TableColumn<Books, Integer> colpages;
    
    @FXML
    private Button btninsert;
    
    @FXML
    private Button btnupdate;
    
    @FXML
    private Button btndelete;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if( event.getSource() == btninsert ){
            insertRecord();
        }else if( event.getSource() == btnupdate ){
            updateRecord();
        }else if( event.getSource() == btndelete ){
            deleteRecord();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBooks();
    }
    
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/libary", "root", "");
            return conn;
        }catch(Exception ex){
            System.out.println("Erro: " + ex.getMessage());
            return null;
        }
    }
    
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM BOOKS";
        Statement st;
        ResultSet rs;
        
        try{
            st =  conn.createStatement();
            rs = st.executeQuery(query);
            
            Books books;
            while( rs.next() ){
                books = new Books(
                   rs.getInt("id"),
                   rs.getString("title"),
                   rs.getString("author"),
                   rs.getInt("year"),
                   rs.getInt("pages")
                );
                bookList.add(books);
            }
            
            tfid.setText("");
            tftitle.setText("");
            tfautor.setText("");
            tfyear.setText("");
            tfpages.setText("");
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return bookList;
        
    }
    
    public void showBooks(){
        ObservableList<Books> list = getBooksList();
        
        colid.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
        coltitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        colauthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        colyear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
        colpages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));
        
        tvBooks.setItems(list);
        
    }
    
    private void insertRecord(){
        String query = "INSERT INTO BOOKS VALUES (" + tfid.getText() + ",'" + tftitle.getText() + "','" + tfautor.getText() + "'," + tfyear.getText() + "," + tfpages.getText() + ")";
        
        executeQuery(query);
        showBooks();
        
    }
    
    private void updateRecord(){
        String query = "UPDATE BOOKS SET TITLE = '" + tftitle.getText() + "', AUTHOR = '" + tfautor.getText() + "', YEAR = '" + tfyear.getText() + "', PAGES = " + tfpages.getText() + " WHERE ID = " + tfid.getText() + "";
        executeQuery(query);
        showBooks();
    }
    
    private void deleteRecord(){
        String query = "DELETE FROM BOOKS WHERE ID =" + tfid.getText() + "";
        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleMouseAction(MouseEvent event) {
        Books book = tvBooks.getSelectionModel().getSelectedItem();
        tfid.setText(""+book.getId());
        tftitle.setText(""+book.getTitle());
        tfautor.setText(""+book.getAuthor());
        tfyear.setText(""+book.getYear());
        tfpages.setText(""+book.getPages());
    }
    
}
