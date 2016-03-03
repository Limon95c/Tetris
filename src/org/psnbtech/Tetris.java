package org.psnbtech;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * The {@code Tetris} class is responsible for handling much of the game logic and
 * reading user input.
 * @author Brendan Jones
 *
 */
public class Tetris extends JFrame {
	
	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = -4722429764792514382L;

	/**
	 * The number of milliseconds per frame.
	 */
	private static final long FRAME_TIME = 1000L / 50L;
	
	/**
	 * The number of pieces that exist.
	 */
	private static final int TYPE_COUNT = TileType.values().length;
		
	/**
	 * The BoardPanel instance.
	 */
	private BoardPanel bplBoardPanel;
	
	/**
	 * The SidePanel instance.
	 */
	private SidePanel splSidePanel;
	
	/**
	 * Whether or not the game is paused.
	 */
	private boolean isPaused;
	
	/**
	 * Whether or not we've played a game yet. This is set to true
	 * initially and then set to false when the game starts.
	 */
	private boolean isNewGame;
	
	/**
	 * Whether or not the game is over.
	 */
	private boolean isGameOver;
	
	/**
	 * The current iLevel we're on.
	 */
	private int iLevel;
	
	/**
	 * The current iScore.
	 */
	private int iScore;
	
	/**
	 * The random number generator. This is used to
	 * spit out pieces randomly.
	 */
	private Random iRandom;
	
	/**
	 * The clock that handles the update logic.
	 */
	private Clock clkLogicTimer;
				
	/**
	 * The current type of tile.
	 */
	private TileType tilCurrentType;
	
	/**
	 * The next type of tile.
	 */
	private TileType tilNextType;
		
	/**
	 * The current column of our tile.
	 */
	private int iCurrentCol;
        
        /**
	 * The name of the file where we can save a game
	 */
        private static final String sNomDatosGuardado = "DatosGuardados.txt";
        
        /**
	 * Archivo de escritura
	 */
        private PrintWriter fileOut;
        
        /**
	 * The current column of our tile.
	 */
        private BufferedReader fileIn;
	
	/**
	 * The current row of our tile.
	 */
	private int iCurrentRow;
	
	/**
	 * The current rotation of our tile.
	 */
	private int iCurrentRotation;
		
	/**
	 * Ensures that a certain amount of time passes after a piece is
	 * spawned before we can drop it.
	 */
	private int iDropCooldown;
	
	/**
	 * The speed of the game.
	 */
	private float fGameSpeed;
        
        /**
	 * The song of the game.
	 */
        private SoundClip auBackMusic;
        
        /**
	 * AudioClips for the Game.
	 */
        private SoundClip auCompleteLine;
        private SoundClip auNewPiece;
		
	/**
	 * Creates a new Tetris instance. Sets up the window's properties,
	 * and adds a controller listener.
	 */
	private Tetris() {
		/*
		 * Set the basic properties of the window.
		 */
		super("Tetris");
                
		setLayout(new BorderLayout());
                
		setDefaultCloseOperation(EXIT_ON_CLOSE);
                
		setResizable(false);
		
		/*
		 * Initialize the BoardPanel and SidePanel instances.
		 */
		this.bplBoardPanel = new BoardPanel(this);
                
		this.splSidePanel = new SidePanel(this);
		
		/*
		 * Add the BoardPanel and SidePanel instances to the window.
		 */
		add(bplBoardPanel, BorderLayout.CENTER);
                
		add(splSidePanel, BorderLayout.EAST);
                
                /**
                * Assign the Music, and other sound Clips
                */
                auBackMusic = new SoundClip ("Dummy.wav");
                
                auCompleteLine = new SoundClip ("FilaCompleta.wav");
                
                auNewPiece = new SoundClip ("NewPiece.wav");
                
                //Set Background Music to Loop
                auBackMusic.setLooping(true);
		
		/*
		 * Adds a custom anonymous KeyListener to the frame.
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent keyEvent) {
								
				switch(keyEvent.getKeyCode()) {
				
				/*
				 * Drop - When pressed, we check to see that the 
				 * game is not paused and that there is no drop 
				 * cooldown, then set the logic timer to run at
                                 * a speed of 25 cycles per second.
				 */
				case KeyEvent.VK_S:
					if(!isPaused && iDropCooldown == 0) {
                                            
					clkLogicTimer.setCyclesPerSecond(25.0f);
                                        
					}
					break;
					
				/*
				 * Move Left - When pressed, we check to see 
				 * that the game is not paused and that the 
                                 * position to the left of the current position 
				 * is valid. If so, we decrement the current 
                                 * column by 1.
				 */
				case KeyEvent.VK_A:
					if(!isPaused &&
                                            bplBoardPanel.isValidAndEmpty(
                                                    tilCurrentType,
                                                    iCurrentCol - 1, 
                                                    iCurrentRow,
                                                    iCurrentRotation)) {
                                            
						iCurrentCol--;
					}
					break;
					
				/*
				 * Move Right - When pressed, we check to see 
				 * that the game is not paused and that the 
				 * position to the right of the current
                                 * position is valid. If so, we increment the 
                                 * current column by 1.
				 */
				case KeyEvent.VK_D:
					if(!isPaused &&
                                            bplBoardPanel.isValidAndEmpty(
                                                    tilCurrentType,
                                                    iCurrentCol + 1, 
                                                    iCurrentRow,
                                                    iCurrentRotation)) {
                                            
						iCurrentCol++;
					}
					break;
					
				/*
				 * Rotate Anticlockwise - When pressed, check to 
				 *  see that the game is not paused and then 
				 * attempt to rotate the piece anticlockwise. 
				 * Because of the size and complexity of the
                                 * rotation code, as well as it's similarity to
                                 * clockwise rotation, the code for rotating 
                                 * the piece is handled in another method.
				 */
				case KeyEvent.VK_Q:
					if(!isPaused) {
						rotatePiece((iCurrentRotation ==
                                                        0) ? 3 :
                                                        iCurrentRotation - 1);
					}
					break;
				
				/*
                                 * Rotate Clockwise - When pressed, check to 
				 * see that the game is not paused and then 
				 * attempt to rotate the piece clockwise. 
				 * Because of the size and complexity of the
                                 * rotation code, as well as it's similarity to
                                 * anticlockwise rotation, the code for rotating
                                 * the piece is handled in another method.
				 */
				case KeyEvent.VK_E:
					if(!isPaused) {
						rotatePiece((iCurrentRotation
                                                        == 3) ? 0 :
                                                        iCurrentRotation + 1);
					}
					break;
                                        
                                /*
                                 * Save Game - When pressed, check to see that
                                 * we're not in a game over. If we're not,
                                 * save the game.
                                 */
                                case KeyEvent.VK_G:
                                    if(!isGameOver && !isNewGame) {
                                        try {
                                            saveGame();
                                        }
                                        catch (IOException e) {
                                            Logger.getLogger(
                                            Tetris.class.getName()).log(
                                                    Level.SEVERE, null, e);
                                        }
                                    }
                                    break;
                                
                                /*
                                 * Load Game - When pressed, check to see that
                                 * we're not in a game over. If we're not,
                                 * load the game.
                                 */
                                case KeyEvent.VK_C:
                                    if(!isGameOver && !isNewGame) {
                                        try {
                                            loadGame();
                                        }
                                        catch (IOException e) {
                                            Logger.getLogger(
                                            Tetris.class.getName()).log(
                                                    Level.SEVERE, null, e);
                                        }
                                    }
                                    break;
					
				/*
				 * Pause Game - When pressed, check to see that 
				 * we're currently playing a game. If so, toggle
                                 * the pause variable and update the logic timer
                                 * to reflect this change, otherwise the game 
				 * will execute a huge number of updates and 
				 * essentially cause an instant game over when
                                 * we unpause if we stay paused for more than a
				 * minute or so.
				 */
				case KeyEvent.VK_P:
                                    
					if(!isGameOver && !isNewGame) {
                                            
                                            isPaused = !isPaused;
                                                
                                            clkLogicTimer.setPaused(isPaused);
                                            
                                            if(isPaused) {
                                            
                                                auBackMusic.stop();
                                            }
                                            else {
                                                
                                                auBackMusic.play();
                                            }
					}
					break;
				
				/*
				 * Start Game - When pressed, check to see that 
				 * we're in either a game over or new game
                                 * state. If so, reset the game.
				 */
				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame) {
						resetGame();
					}
					break;
				
				}
			}
			
			@Override
			public void keyReleased(KeyEvent keyEvent) {
				
                            switch(keyEvent.getKeyCode()) {
				
                            /*
                             * Drop - When released, we set the speed of 
                             * the logic timer back to whatever the current 
                             * game speed is and clear out any cycles that 
                             * might still be elapsed.
                             */
                            case KeyEvent.VK_S:
                                    
                                clkLogicTimer.setCyclesPerSecond(fGameSpeed);
                                      
                                clkLogicTimer.reset();
                                    
                                break;
                            }
				
			}
			
		});
		
		/*
		 * Here we resize the frame to hold the BoardPanel and SidePanel 
		 * instances, center the window on the screen, and show it to
                 * the user. 
		 */
                
		pack();
                
		setLocationRelativeTo(null);
                
		setVisible(true);
	}
	
	/**
	 * Starts the game running. Initializes everything and enters the game 
         * loop.
	 */
	private void startGame() {
		/*
		 * Initialize our random number generator, music ,logic timer,
                 * and new game variables.
		 */
		this.iRandom = new Random();
		this.isNewGame = true;
		this.fGameSpeed = 1.0f;
                
                auBackMusic.play();
		
		/*
		 * Setup the timer to keep the game from running before the
                 * user presses enter to start it.
		 */
		this.clkLogicTimer = new Clock(fGameSpeed);
                
		clkLogicTimer.setPaused(true);
		
		while(true) {
			//Get the time that the frame started.
			long start = System.nanoTime();
			
			//Update the logic timer.
			clkLogicTimer.update();
			
			/*
			 * If a cycle has elapsed on the timer, we can update 
                         * the game and
			 * move our current piece down.
			 */
                        
			if(clkLogicTimer.hasElapsedCycle()) {
                            
				updateGame();
			}
		
			//Decrement the drop cool down if necessary.
			if(iDropCooldown > 0) {
                            
				iDropCooldown--;
			}
			
			//Display the window to the user.
			renderGame();
			
			/*
			 * Sleep to cap the framerate.
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
                        
			if(delta < FRAME_TIME) {
                            
				try {
                                    
					Thread.sleep(FRAME_TIME - delta);
                                        
				} catch(Exception e) {
                                    
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Updates the game and handles the bulk of it's logic.
	 */
	private void updateGame() {
		/*
		 * Check to see if the piece's position can move down to the 
                 * next row.
		 */
		if(bplBoardPanel.isValidAndEmpty(tilCurrentType, iCurrentCol, 
                        iCurrentRow + 1, iCurrentRotation)) {
                    
			//Increment the current row if it's safe to do so.
                        
			iCurrentRow++;
		} else {
			/*
			 * We've either reached the bottom of the board, or 
                         * landed on another piece, so
			 * we need to add the piece to the board.
			 */
			bplBoardPanel.addPiece(tilCurrentType, iCurrentCol, 
                                iCurrentRow, iCurrentRotation);
			
			/*
			 * Check to see if adding the new piece resulted in 
			 * any cleared lines. If so, increase the player's 
			 * iScore. (Up to 4 lines can be cleared in a single
                         * go; [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts])
			 */
                        
			int iCleared = bplBoardPanel.checkLines();
                        
			if(iCleared > 0) {
                            
				iScore += 50 << iCleared;
                                
                                auCompleteLine.play();
			}
			
			/*
			 * Increase the speed slightly for the next piece and 
                         * update the game's timer
			 * to reflect the increase.
			 */
			fGameSpeed += 0.035f;
                        
			clkLogicTimer.setCyclesPerSecond(fGameSpeed);
                        
			clkLogicTimer.reset();
			
			/*
			 * Set the drop cooldown so the next piece doesn't 
			 * automatically come flying in from the heavens 
			 * immediately after this piece hits if we've not
                         * reacted yet. (~0.5 second buffer).
			 */
                        
			iDropCooldown = 25;
			
			/*
			 * Update the difficulty iLevel. This has no effect on 
			 * the game, and is only used in the "Level" string
                         * in the SidePanel. 
			 */
			iLevel = (int)(fGameSpeed * 1.70f);
			
			/*
			 * Spawn a new piece to control.
			 */
			spawnPiece();
                        
                        //Play the new piece AudioClip
                        auNewPiece.play();
		}		
	}
	
	/**
	 * Forces the BoardPanel and SidePanel to repaint.
	 */
	private void renderGame() {
            
		bplBoardPanel.repaint();
                
		splSidePanel.repaint();
	}
	
	/**
	 * Resets the game variables to their default values at the start
	 * of a new game.
	 */
	private void resetGame() {
            
		this.iLevel = 1;
                
		this.iScore = 0;
                
		this.fGameSpeed = 1.0f;
                
		this.tilNextType = TileType.values()
                        [iRandom.nextInt(TYPE_COUNT)];
                
		this.isNewGame = false;
                
		this.isGameOver = false;	
                
		bplBoardPanel.clear();
                
		clkLogicTimer.reset();
                
		clkLogicTimer.setCyclesPerSecond(fGameSpeed);
                
		spawnPiece();
	}
		
	/**
	 * Spawns a new piece and resets our piece's variables to their default
	 * values.
	 */
	private void spawnPiece() {
            
		/*
		 * Poll the last piece and reset our position and rotation to
		 * their default variables, then pick the next piece to use.
		 */
		this.tilCurrentType = tilNextType;
                
		this.iCurrentCol = tilCurrentType.getSpawnColumn();
                
		this.iCurrentRow = tilCurrentType.getSpawnRow();
                
		this.iCurrentRotation = 0;
                
		this.tilNextType = TileType.values()
                        [iRandom.nextInt(TYPE_COUNT)];
		
		/*
		 * If the spawn point is invalid, we need to pause the game and 
		 * flag that we've lost because it means that the pieces on
                 * the board have gotten too high. 
		 */
		if(!bplBoardPanel.isValidAndEmpty(tilCurrentType, iCurrentCol, 
                        iCurrentRow, iCurrentRotation)) {
                    
			this.isGameOver = true;
                        
			clkLogicTimer.setPaused(true);
		}		
	}

	/**
	 * Attempts to set the rotation of the current piece to newRotation.
	 * @param iNewRotation The rotation of the new peice.
	 */
	private void rotatePiece(int iNewRotation) {
		/*
		 * Sometimes pieces will need to be moved when rotated to 
		 * avoid clipping out of the board (the I piece is a good 
		 * example of this). Here we store a temporary row and column
                 * in case we need to move the tile as well.
		 */
		int iNewCol = iCurrentCol;
		int iNewRow = iCurrentRow;
		
		/*
		 * Get the insets for each of the sides. These are used to 
                 * determine how
		 * many empty rows or columns there are on a given side.
		 */
                
		int iLeft = tilCurrentType.getLeftInset(iNewRotation);
                
		int iRight = tilCurrentType.getRightInset(iNewRotation);
                
		int iTop = tilCurrentType.getTopInset(iNewRotation);
                
		int iBottom = tilCurrentType.getBottomInset(iNewRotation);
		
		/*
		 * If the current piece is too far to the left or right, move
                 * the piece away from the edges
		 * so that the piece doesn't clip out of the map and
                 * automatically become invalid.
		 */
                
		if(iCurrentCol < -iLeft) {
                    
			iNewCol -= iCurrentCol - iLeft;
                        
		} else if(iCurrentCol + tilCurrentType.getDimension() - iRight 
                        >= BoardPanel.COL_COUNT) {
                    
			iNewCol -= (iCurrentCol + tilCurrentType.getDimension() 
                                - iRight) - BoardPanel.COL_COUNT + 1;
		}
		
		/*
		 * If the current piece is too far to the top or bottom, move
                 * the piece away from the edges
		 * so that the piece doesn't clip out of the map and
                 * automatically become invalid.
		 */
                
		if(iCurrentRow < -iTop) {
                    
			iNewRow -= iCurrentRow - iTop;
                        
		} else if(iCurrentRow + tilCurrentType.getDimension() - iBottom 
                        >= BoardPanel.ROW_COUNT) {
                    
			iNewRow -= (iCurrentRow + tilCurrentType.getDimension() 
                                - iBottom) - BoardPanel.ROW_COUNT + 1;
		}
		
		/*
		 * Check to see if the new position is acceptable.
                 * If it is, update the rotation and
		 * position of the piece.
		 */
		if(bplBoardPanel.isValidAndEmpty(tilCurrentType, iNewCol, 
                        iNewRow, iNewRotation)) {
                    
			iCurrentRotation = iNewRotation;
                        
			iCurrentRow = iNewRow;
                        
			iCurrentCol = iNewCol;
		}
	}
	
	/**
	 * Checks to see whether or not the game is paused.
	 * @return Whether or not the game is paused.
	 */
	public boolean isPaused() {
            
		return isPaused;
	}
	
	/**
	 * Checks to see whether or not the game is over.
	 * @return Whether or not the game is over.
	 */
	public boolean isGameOver() {
            
		return isGameOver;
	}
	
	/**
	 * Checks to see whether or not we're on a new game.
	 * @return Whether or not this is a new game.
	 */
	public boolean isNewGame() {
            
		return isNewGame;
	}
	
	/**
	 * Gets the current iScore.
	 * @return The iScore.
	 */
	public int getScore() {
		return iScore;
	}
	
	/**
	 * Gets the current iLevel.
	 * @return The iLevel.
	 */
	public int getLevel() {
            
		return iLevel;
	}
	
	/**
	 * Gets the current type of piece we're using.
	 * @return The piece type.
	 */
	public TileType getPieceType() {
            
		return tilCurrentType;
	}
	
	/**
	 * Gets the next type of piece we're using.
	 * @return The next piece.
	 */
	public TileType getNextPieceType() {
            
		return tilNextType;
	}
	
	/**
	 * Gets the column of the current piece.
	 * @return The column.
	 */
	public int getPieceCol() {
            
		return iCurrentCol;
	}
	
	/**
	 * Gets the row of the current piece.
	 * @return The row.
	 */
	public int getPieceRow() {
            
		return iCurrentRow;
	}
	
	/**
	 * Gets the rotation of the current piece.
	 * @return The rotation.
	 */
	public int getPieceRotation() {
            
		return iCurrentRotation;
	}
        
        /**
	 * Saves the actual game
	 */
        public void saveGame() throws IOException {
            // Abrir archivo
            fileOut = new PrintWriter(new FileWriter(sNomDatosGuardado));
            fileOut.println(Integer.toString(iLevel)); // Guardar nivel
            fileOut.println(Integer.toString(iScore)); // Guardar score
            fileOut.println(Float.toString(fGameSpeed)); // Guardar gameSpeed
            
            guardaTileActualYEstado(fileOut);
            
            // Tile siguiente
            fileOut.println(Integer.toString(tilNextType.ordinal()));
            
            // Board
            for(int iI = 0; iI < bplBoardPanel.getROW() ; iI++) {
                for(int iJ = 0; iJ < bplBoardPanel.getCOL(); iJ++) {
                    if(bplBoardPanel.getTile(iJ, iI) != null) {
                        fileOut.println(Integer.toString(iI));
                        fileOut.println(Integer.toString(iJ));
                        fileOut.println(Integer.toString(
                                bplBoardPanel.getTile(iJ, iI).ordinal()));
                    }
                }
            }
            fileOut.close();
        }
        
        public void guardaTileActualYEstado(PrintWriter fileOut) {
            // Numero del Tile Actual
            fileOut.println(Integer.toString(tilCurrentType.ordinal()));
            fileOut.println(Integer.toString(iCurrentCol)); // Guardar ColumnaAc
            // Guardar Rotacion Actual
            fileOut.println(Integer.toString(iCurrentRotation));
            fileOut.println(Integer.toString(iCurrentRow)); // Guardar fila act
            fileOut.println(Integer.toString(iDropCooldown)); // Guardar cool...
            
            // Estado de la partida
            if(isPaused) {
                fileOut.println(Integer.toString(1)); // Pausado(1) o no(0)
            }
            else {
                fileOut.println(Integer.toString(0)); // Pausado(1) o no(0)
            }
            
            if(isNewGame) {
                fileOut.println(Integer.toString(1)); // NewGame(1) o no(0)
            }
            else {
                fileOut.println(Integer.toString(0)); // NewGame(1) o no(0)
            }
        }
        
        public void loadGame() throws IOException {
            int iPausaGuardada, iNewGame;
            String sScan;
            // Abrimos el archivo en caso de que hubiera uno
            try {
                // Abrir el archivo
                isPaused = true;
                fileIn = new BufferedReader(new FileReader(sNomDatosGuardado));
                iLevel = Integer.parseInt(fileIn.readLine());
                iScore = Integer.parseInt(fileIn.readLine());
                fGameSpeed = Float.parseFloat(fileIn.readLine());
                tilCurrentType = TileType.values()
                        [Integer.parseInt(fileIn.readLine())];
                iCurrentCol = Integer.parseInt(fileIn.readLine());
                iCurrentRotation = Integer.parseInt(fileIn.readLine());
                iCurrentRow = Integer.parseInt(fileIn.readLine());
                iDropCooldown = Integer.parseInt(fileIn.readLine());
                iPausaGuardada = Integer.parseInt(fileIn.readLine());
                iNewGame = Integer.parseInt(fileIn.readLine());
                tilNextType = TileType.values()
                        [Integer.parseInt(fileIn.readLine())];
                for(int iI = 0; iI < bplBoardPanel.getROW() ; iI++) {
                    for(int iJ = 0; iJ < bplBoardPanel.getCOL(); iJ++) {
                        bplBoardPanel.nullTile(iJ, iI);
                    }
                }
                sScan = fileIn.readLine();
                while(sScan != null) {
                    int iColum = Integer.parseInt(fileIn.readLine());
                    bplBoardPanel.setTile(iColum,
                            Integer.parseInt(sScan),
                            TileType.values()
                                    [Integer.parseInt(fileIn.readLine())]);
                    sScan = fileIn.readLine();
                }
                if(iNewGame == 1) {
                    isNewGame = true;
                }
                else {
                    isNewGame = false;
                }
                
                if(iPausaGuardada == 1) {
                    isPaused = true;
                }
                else {
                    isPaused = false;
                }
                fileIn.close();
            }
            catch (FileNotFoundException e){
                // Si no se encuentra archivo guardado no cargar nada
            }
        }

	/**
	 * Entry-point of the game. Responsible for creating and starting a new
	 * game instance.
	 * @param args Unused.
	 */
	public static void main(String[] args) {
            
		Tetris tetris = new Tetris();
                
		tetris.startGame();
	}

}
