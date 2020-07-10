import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.stage.Stage; 
import javafx.stage.Screen; 
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.scene.paint.*; 
import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.HBox; 
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.input.*;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
public class dummy extends Application{
    // main class
	List<song> slist;
	String[] read(String title) throws Exception{
		File file = new File("sample-song.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		String arr[];
		// reading file
		while((st = br.readLine()) != null){
			arr = st.split("	");
			if(arr[0].equalsIgnoreCase(title.trim())){
				return(arr);
			}
		}
		return null;
	}
	
	public static class song{
	    // Properties for a track, Url is for location
		private final SimpleStringProperty title;
		private final SimpleStringProperty artist;
		private final SimpleStringProperty duration;
	 	private final SimpleStringProperty url;

	    // Constructor of the class which sets the values passed when the class call is made
		private song(String titlename, String artistname, String len, String loc) {
			this.title = new SimpleStringProperty(titlename);
			this.artist = new SimpleStringProperty(artistname);
			this.duration = new SimpleStringProperty(len);
			this.url = new SimpleStringProperty(loc);
		}

	    // methods to get values or set the values of the current object
		public String getTitle() {
			return title.get();
		}
		public void setTitle(String titlename) {
			title.set(titlename);
		}
			
		public String getArtist() {
			return artist.get();
		}
		public void setArtist(String artistname) {
			artist.set(artistname);
		}
		
		public String getDuration() {
			return duration.get();
		}
		public void setDuration(String len) {
			duration.set(len);
		}     

		public String getURL() {
			return url.get();
		}
		public void setURL(String loc) {
			url.set(loc);
		}     

	}
	
	final ObservableList<song> mlist = FXCollections.observableArrayList(new song("Gorgeous","Property Prophets","2:62","test.mp4"));	
	
	void playlistwindow(Stage stage){
	    // search box
		TextField search = new TextField("search song");
		search.setPromptText("Search Songs");

		// Observable list for the search result
		final ObservableList<song> getres = FXCollections.observableArrayList();		
		final TableView<song> result = new TableView<>();

		// Setting style of the columns and table.
		result.setEditable(false);
		result.setPrefHeight(100);
		
		TableColumn titlecol = new TableColumn("Title");
		titlecol.setPrefWidth(400);
		titlecol.setResizable(false);
		titlecol.setCellValueFactory(new PropertyValueFactory<song,String>("title"));
		
		TableColumn artistcol = new TableColumn("Artist");
		artistcol.setPrefWidth(300);
		artistcol.setResizable(false);
		artistcol.setCellValueFactory(new PropertyValueFactory<song,String>("artist"));
			
		TableColumn durationcol = new TableColumn("Duration");
		durationcol.setPrefWidth(100);
		durationcol.setResizable(false);
		durationcol.setCellValueFactory(new PropertyValueFactory<song,String>("duration"));

		result.setItems(getres);				
		result.getColumns().addAll(titlecol,artistcol,durationcol);

		/* Function triggers when enter is pressed in search box. If track exists it returns 4 strings back and then its added to the Observable list. If nothing is found, null is returned. It also clears old results */
		search.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					try{
						if(!(getres.isEmpty())) getres.remove(0);
						String arr[] = read(search.getText());
						String[] ts = arr[2].split("");
						getres.add(new song(arr[0],arr[1],ts[0]+":"+ts[1]+ts[2],arr[3]));
						search.clear();
					}
					catch(Exception e){
					    System.out.println("Something went wrong in search function");}
				}
			}
		});
		
						
		Button add = new Button("add");

		// table for actual playlist and its properties like the search one
		final TableView<song> mview = new TableView<>();

		mview.setEditable(false);
		mview.setPrefHeight(500);
		
		TableColumn mtitlecol = new TableColumn("Title");
		mtitlecol.setPrefWidth(400);
		mtitlecol.setResizable(false);
		mtitlecol.setCellValueFactory(new PropertyValueFactory<song,String>("title"));
		
		TableColumn martistcol = new TableColumn("Artist");
		martistcol.setPrefWidth(300);
		martistcol.setResizable(false);
		martistcol.setCellValueFactory(new PropertyValueFactory<song,String>("artist"));
			
		TableColumn mdurationcol = new TableColumn("Duration");
		mdurationcol.setPrefWidth(100);
		mdurationcol.setResizable(false);
		mdurationcol.setCellValueFactory(new PropertyValueFactory<song,String>("duration"));

		mview.setItems(mlist);				
		mview.getColumns().addAll(mtitlecol,martistcol,mdurationcol);

		// When add button is clicked it add the result from the search result to the playlist table.
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				if(!(mlist.contains(getres.get(0))))
				mlist.add(getres.get(0));
			}					
		});
		
		Button del = new Button("delete");

		// Delete button, when its pressed and song is selected in playlist it will be deleted from it
		del.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				song s = mview.getSelectionModel().getSelectedItem();
				mlist.remove(mlist.indexOf(mview.getSelectionModel().getSelectedItem()));
			}					
		});
		
		
		Button done = new Button("done");

		// a layout for grid pane
		GridPane gp = new GridPane();
		gp.addRow(0,search);
		gp.addRow(1,result);
		gp.addRow(2,add);
		gp.addRow(3,mview);
		gp.addRow(4,del);
		gp.addRow(5,done);
		
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		VBox vb = new VBox();
		vb.getChildren().addAll(gp);
		Scene scene2 = new Scene(vb, 1000, 800);
		Stage playlist = new Stage();
		playlist.setTitle("Playlist");
		playlist.setScene(scene2);
		playlist.setResizable(false);
		playlist.setX(stage.getX() + 200);
		playlist.setY(stage.getY() + 50);
		playlist.show();
		
		// When you press "done" it gets mlist(ObservableArrayList of playlist and converts it to a list 
		done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				slist = mlist;
				itr = slist.iterator();
				playlist.close();
			}					
		});

	}
		
    final MediaView mv = new MediaView(); // its global because in start function bindings use mv
	Button nf = new Button("Next");
		
	Button pp = new Button("Pause");
    Iterator<song> itr = mlist.iterator(); // iterator initially listening to mlist
    
    void playsong(Stage stage){ // This stage is passed as args because we need the original stage to set the window title everytime the song changes

	// its init with mlist(observablearraylist) but after choosing the playlist its assigned to slist. 
		song s = itr.next();
		stage.setTitle(s.getTitle() + " - " + s.getArtist()); // setting title
		File f = new File(s.getURL()); // getting file location from struct getURL method
		Media media = new Media(f.toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        mv.setMediaPlayer(player); // mv is globally initialised
        player.play(); // plays the track
	// when track ends and the list has next value then it calls the same playsong function with the same stage.
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
				player.stop();
                if(itr.hasNext()) {
                    playsong(stage);
			    }
                return;
            }
        });

	// Simple check when pressed play or pause. 
		pp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                if(player.getStatus().equals(player.getStatus().PLAYING)){
					player.pause();
					pp.setText("Play");
				}
				else{
					player.play();
					pp.setText("Pause");
				}
            }
        });

		// "next" track button. it stops the player, cheks the lists, if it has next value it calls itself again
		nf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
				player.stop();
				  if(itr.hasNext()) {
                    playsong(stage);
                }
			}					
        });
		
    }
	@Override
	// main stage
	public void start(Stage stage) throws Exception{
		Button lst = new Button("List");
		
		playsong(stage);
		// below is just simple properties and panes
		HBox hb = new HBox(10);
		hb.setPadding(new Insets(10));
		hb.getChildren().addAll(pp,nf,lst);
		hb.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
			
		BorderPane bp = new BorderPane(mv,null,null,hb,null);
		hb.setAlignment(Pos.CENTER);
		bp.setAlignment(mv,Pos.CENTER);
		bp.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY, Insets.EMPTY)));
		
		Scene scene = new Scene(bp);
		stage.setMaximized(true);
		final DoubleProperty width = mv.fitWidthProperty();
		final DoubleProperty height = mv.fitHeightProperty();
		width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(mv.sceneProperty(), "height").subtract(100));
		// Action event which triggers the playlist window
		lst.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
				playlistwindow(stage);
			}					
        });
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(720);
		stage.show();
	}
		
	public static void main(String args[]){
		launch(args);
	}
}
