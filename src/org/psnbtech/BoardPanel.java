package org.psnbtech;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The {@code BoardPanel} class is responsible for displaying the game grid and
 * handling things related to the game board.
 * @author Brendan Jones
 *
 */
public class BoardPanel extends JPanel {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 5055679736784226108L;

	/**
	 * Minimum color component values for tiles. This is required if we
	 * want to show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MIN = 35;
	
	/**
	 * Maximum color component values for tiles. This is required if we
	 * want to show both light and dark shading on our tiles.
	 */
	public static final int COLOR_MAX = 255 - COLOR_MIN;
	
	/**
	 * The width of the border around the game board.
	 */
	private static final int BORDER_WIDTH = 5;
	
	/**
	 * The number of columns on the board.
	 */
	public static final int COL_COUNT = 10;
		
	/**
	 * The number of visible rows on the board.
	 */
	private static final int VISIBLE_ROW_COUNT = 20;
	
	/**
	 * The number of rows that are hidden from view.
	 */
	private static final int HIDDEN_ROW_COUNT = 2;
	
	/**
	 * The total number of rows that the board contains.
	 */
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + 
                HIDDEN_ROW_COUNT;
	
	/**
	 * The number of pixels that a tilTile takes up.
	 */
	public static final int TILE_SIZE = 24;
	
	/**
	 * The width of the shading on the tiles.
	 */
	public static final int SHADE_WIDTH = 4;
	
	/**
	 * The central x coordinate on the game board.
	 */
	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	
	/**
	 * The central y coordinate on the game board.
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		
	/**
	 * The total width of the panel.
	 */
	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + 
                BORDER_WIDTH * 2;
	
	/**
	 * The total height of the panel.
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + 
                BORDER_WIDTH * 2;
	
	/**
	 * The larger font to display.
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", 
                Font.BOLD, 16);

	/**
	 * The smaller font to display.
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", 
                Font.BOLD, 12);
	
	/**
	 * The Tetris instance.
	 */
	private Tetris tetTetris;
	
	/**
	 * The tiles that make up the board.
	 */
	private TileType[][] tiles;
		
	/**
	 * Crates a new GameBoard instance.
	 * @param tetris The Tetris instance to use.
	 */
	public BoardPanel(Tetris tetris) {
		this.tetTetris = tetris;
		this.tiles = new TileType[ROW_COUNT][COL_COUNT];
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	/**
	 * Resets the board and clears away any tiles.
	 */
	public void clear() {
		/*
		 * Loop through every tilTile index and set it's value
		 * to null to clear the board.
		 */
		for(int iI = 0; iI < ROW_COUNT; iI++) {
			for(int iJ = 0; iJ < COL_COUNT; iJ++) {
				tiles[iI][iJ] = null;
			}
		}
	}
	
	/**
	 * Determines whether or not a piece can be placed at the coordinates.
	 * @param tilType THe tilType of piece to use.
	 * @param iX The iX coordinate of the piece.
	 * @param iY The iY coordinate of the piece.
	 * @param iRotation The iRotation of the piece.
	 * @return Whether or not the position is valid.
	 */
	public boolean isValidAndEmpty(TileType tilType, int iX, int iY, 
                int iRotation) {
				
		//Ensure the piece is in a valid column.
		if(iX < -tilType.getLeftInset(iRotation) || iX + 
                        tilType.getDimension() - 
                        tilType.getRightInset(iRotation) >= COL_COUNT) {
			return false;
		}
		
		//Ensure the piece is in a valid iRow.
		if(iY < -tilType.getTopInset(iRotation) || iY + 
                        tilType.getDimension() - 
                        tilType.getBottomInset(iRotation) >= ROW_COUNT) {
			return false;
		}
		
		/*
		 * Loop through every tilTile in the piece and see if it 
		 * conflicts with an existing tilTile.
                 *
		 * Note: It's fine to do this even though it allows for wrapping 
                 * because we've already
		 * checked to make sure the piece is in a valid location.
		 */
		for(int iCol = 0; iCol < tilType.getDimension(); iCol++) {
			for(int iRow = 0; iRow < tilType.getDimension(); 
                                iRow++) {
				if(tilType.isTile(iCol, iRow, iRotation) && 
                                        isOccupied(iX + iCol, iY + iRow)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Adds a piece to the game board. Note: Doesn't check for existing 
         * pieces,
	 * and will overwrite them if they exist.
	 * @param tilType The tilType of piece to place.
	 * @param iX The iX coordinate of the piece.
	 * @param iY The iY coordinate of the piece.
	 * @param iRotation The iRotation of the piece.
	 */
	public void addPiece(TileType tilType, int iX, int iY, int iRotation) {
		/*
		 * Loop through every tilTile within the piece and add it
		 * to the board only if the boolean that represents that
		 * tilTile is set to true.
		 */
		for(int col = 0; col < tilType.getDimension(); col++) {
			for(int row = 0; row < tilType.getDimension(); row++) {
				if(tilType.isTile(col, row, iRotation)) {
					setTile(col + iX, row + iY, tilType);
				}
			}
		}
	}
	
	/**
	 * Checks the board to see if any lines have been cleared, and
	 * removes them from the game.
	 * @return The number of lines that were cleared.
	 */
	public int checkLines() {
		int iCompletedLines = 0;
		
		/*
		 * Here we loop through every line and check it to see if
		 * it's been cleared or not. If it has, we increment the
		 * number of completed lines and check the next iRow.
		 * 
		 * The checkLine function handles clearing the line and
		 * shifting the rest of the board down for us.
		 */
		for(int iRow = 0; iRow < ROW_COUNT; iRow++) {
			if(checkLine(iRow)) {
				iCompletedLines++;
			}
		}
		return iCompletedLines;
	}
			
	/**
	 * Checks whether or not {@code iRow} is full.
	 * @param iLines The iRow to check.
	 * @return Whether or not this iRow is full.
	 */
	private boolean checkLine(int iLines) {
		/*
		 * Iterate through every column in this iRow. If any of them are
		 * empty, then the iRow is not full.
		 */
		for(int iCol = 0; iCol < COL_COUNT; iCol++) {
			if(!isOccupied(iCol, iLines)) {
				return false;
			}
		}
		
		/*
		 * Since the iLines is filled, we need to 'remove' it from the 
                 * game.
		 * To do this, we simply shift every iRow above it down by one.
		 */
		for(int iRows = iLines - 1; iRows >= 0; iRows--) {
			for(int iCols = 0; iCols < COL_COUNT; iCols++) {
				setTile(iCols, iRows + 1, 
                                        getTile(iCols, iRows));
			}
		}
		return true;
	}
	
	
	/**
	 * Checks to see if the tilTile is already occupied.
	 * @param iX The x coordinate to check.
	 * @param iY The y coordinate to check.
	 * @return Whether or not the tilTile is occupied.
	 */
	private boolean isOccupied(int iX, int iY) {
		return tiles[iY][iX] != null;
	}
	
	/**
	 * Sets a tilTile located at the desired column and iRow.
	 * @param iX The column.
	 * @param iY The iRow.
	 * @param tilType The value to set to the tilTile to.
	 */
	private void setTile(int  iX, int iY, TileType tilType) {
		tiles[iY][iX] = tilType;
	}
		
	/**
	 * Gets a tilTile by it's column and iRow.
	 * @param iX The column.
	 * @param iY The iRow.
	 * @return The tilTile.
	 */
	private TileType getTile(int iX, int iY) {
		return tiles[iY][iX];
	}
	
	@Override
	public void paintComponent(Graphics graGraphic) {
		super.paintComponent(graGraphic);
		
		//This helps simplify the positioning of things.
		graGraphic.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		/*
		 * Draw the board differently depending on the current game 
                 * state.
		 */
		if(tetTetris.isPaused()) {
                    
			graGraphic.setFont(LARGE_FONT);
                        
			graGraphic.setColor(Color.WHITE);
                        
			String msg = "PAUSED";
                        
			graGraphic.drawString(msg, CENTER_X - 
                                graGraphic.getFontMetrics().stringWidth(msg) 
                                        / 2, CENTER_Y);
                        
		} else if(tetTetris.isNewGame() || tetTetris.isGameOver()) {
                    
			graGraphic.setFont(LARGE_FONT);
                        
			graGraphic.setColor(Color.WHITE);
			
			/*
			 * Because both the game over and new game screens 
                         * are nearly identical,
			 * we can handle them together and just use a 
                         * ternary operator to change
			 * the messages that are displayed.
			 */
			String sMsg = tetTetris.isNewGame() 
                                ? "TETRIS" : "GAME OVER";
                        
			graGraphic.drawString(sMsg, CENTER_X - 
                                graGraphic.getFontMetrics().stringWidth(sMsg) 
                                        / 2, 150);
                        
			graGraphic.setFont(SMALL_FONT);
                        
			sMsg = "Press Enter to Play" + 
                                (tetTetris.isNewGame() ? "" : " Again");
                        
			graGraphic.drawString(sMsg, CENTER_X - 
                                graGraphic.getFontMetrics().stringWidth(sMsg) 
                                        / 2, 300);
		} else {
			
			/*
			 * Draw the tiles onto the board.
			 */
			for(int iX = 0; iX < COL_COUNT; iX++) {
                            
				for(int iY = HIDDEN_ROW_COUNT; 
                                        iY < ROW_COUNT; iY++) {
                                    
					TileType tilTile = getTile(iX, iY);
                                        
					if(tilTile != null) {
                                            
						drawTile(tilTile, iX * TILE_SIZE, 
                                                        (iY - HIDDEN_ROW_COUNT) 
                                                                * TILE_SIZE, 
                                                                    graGraphic);
					}
				}
			}
			
			/*
			 * Draw the current piece. This cannot be drawn like the 
                         * rest of the pieces because it's still not part of the 
			 * game board. If it were part of the board, it would 
			 * need to be removed every frame which would just be 
			 * slow and confusing.
			 */
			TileType tilType = tetTetris.getPieceType();
			int iPieceCol = tetTetris.getPieceCol();
			int iPieceRow = tetTetris.getPieceRow();
			int iRotation = tetTetris.getPieceRotation();
			
			//Draw the piece onto the board.
			for(int iCol = 0; iCol < tilType.getDimension(); 
                                iCol++) {
                            
				for(int iRows = 0; iRows < 
                                        tilType.getDimension(); iRows++) {
                                    
					if(iPieceRow + iRows >= 2 && 
                                                tilType.isTile(iCol, iRows, 
                                                        iRotation)) {
                                            
						drawTile(tilType, 
                                                        (iPieceCol + iCol) * 
                                                                TILE_SIZE, 
                                                        (iPieceRow + iRows - 
                                                               HIDDEN_ROW_COUNT) 
                                                                * TILE_SIZE, 
                                                                graGraphic);
					}
				}
			}
			
			/*
			 * Draw the ghost (semi-transparent piece that shows 
                         * where the current piece will land). I couldn't think 
			 * of a better way to implement this so it'll have to do 
			 * for now. We simply take the current position and move
                         * down until we hit a iRow that would cause a collision
			 */
			Color clrBase = tilType.getBaseColor();
			clrBase = new Color(clrBase.getRed(), clrBase.getGreen()
                                , clrBase.getBlue(), 20);
			for(int iLowest = iPieceRow; iLowest < 
                                ROW_COUNT; iLowest++) {
                            
				//If no collision is detected, try the next iRow
				if(isValidAndEmpty(tilType, iPieceCol, 
                                        iLowest, iRotation)) {
                                    
					continue;
				}
				
				//Draw the ghost one iRow higher than the one 
                                //the collision took place at.
				iLowest--;
				
				//Draw the ghost piece.
				for(int iCol = 0; iCol < tilType.getDimension(); 
                                        iCol++) {
                                    
					for(int iRow = 0; iRow < 
                                                tilType.getDimension(); iRow++) {
                                            
						if(iLowest + iRow >= 2 && 
                                                        tilType.isTile(iCol, iRow, 
                                                                iRotation)) {
							drawTile(clrBase, 
                                                             clrBase.brighter(), 
                                                             clrBase.darker(), 
                                                             (iPieceCol + iCol) * 
                                                                     TILE_SIZE, 
                                                             (iLowest + iRow - 
                                                               HIDDEN_ROW_COUNT) 
                                                                    * TILE_SIZE, 
                                                             graGraphic);
						}
					}
				}
				
				break;
			}
			
			/*
			 * Draw the background grid above the pieces 
                         * (serves as a useful visual
			 * for players, and makes the pieces look nicer by 
                         * breaking them up.
			 */
                        
			graGraphic.setColor(Color.DARK_GRAY);
			for(int iX = 0; iX < COL_COUNT; iX++) {
				for(int iY = 0; iY < VISIBLE_ROW_COUNT; iY++) {
					graGraphic.drawLine(0, iY * TILE_SIZE, 
                                                COL_COUNT * TILE_SIZE, iY * 
                                                        TILE_SIZE);
					graGraphic.drawLine(iX * TILE_SIZE, 0, 
                                                iX * TILE_SIZE, 
                                                VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}
		
		/*
		 * Draw the outline.
		 */
		graGraphic.setColor(Color.WHITE);
                
		graGraphic.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * 
                        VISIBLE_ROW_COUNT);
	}
	
	/**
	 * Draws a tilTile onto the board.
	 * @param tilType The type of tilTile to draw.
	 * @param iX The column.
	 * @param iY The iRow.
	 * @param graGraphics The graphics object.
	 */
	private void drawTile(TileType tilType, int iX, int iY, Graphics 
                graGraphics) {
            
		drawTile(tilType.getBaseColor(), tilType.getLightColor(), 
                        tilType.getDarkColor(), iX, iY, graGraphics);
	}
	
	/**
	 * Draws a tilTile onto the board.
	 * @param clrBase The base color of tilTile.
	 * @param clrLight The light color of the tilTile.
	 * @param clrDark The dark color of the tilTile.
	 * @param iX The column.
	 * @param iY The iRow.
	 * @param graGraphics The graphics object.
	 */
	private void drawTile(Color clrBase, Color clrLight, Color clrDark, 
                int iX, int iY, Graphics graGraphics) {
		
		/*
		 * Fill the entire tilTile with the base color.
		 */
		graGraphics.setColor(clrBase);
                
		graGraphics.fillRect(iX, iY, TILE_SIZE, TILE_SIZE);
		
		/*
		 * Fill the bottom and right edges of the tilTile 
                 * with the dark shading color.
		 */
		graGraphics.setColor(clrDark);
                
		graGraphics.fillRect(iX, iY + TILE_SIZE - SHADE_WIDTH, 
                        TILE_SIZE, SHADE_WIDTH);
                
		graGraphics.fillRect(iX + TILE_SIZE - SHADE_WIDTH, iY, 
                        SHADE_WIDTH, TILE_SIZE);
		
		/*
		 * Fill the top and left edges with the light shading. 
                 * We draw a single line for each iRow or column rather than a 
		 * rectangle so that we can draw a nice looking diagonal where 
		 * the light and dark shading meet.
		 */
		graGraphics.setColor(clrLight);
                
		for(int iI = 0; iI < SHADE_WIDTH; iI++) {
                    
			graGraphics.drawLine(iX, iY + iI, iX + 
                                TILE_SIZE - iI - 1, iY + iI);
                        
			graGraphics.drawLine(iX + iI, iY, iX + iI, iY + 
                                TILE_SIZE - iI - 1);
		}
	}

}
