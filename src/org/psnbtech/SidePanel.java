package org.psnbtech;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The {@code SidePanel} class is responsible for displaying various information
 * on the game such as the next piece, the score and current level, and controls.
 * @author Brendan Jones
 *
 */
public class SidePanel extends JPanel {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 2181495598854992747L;

	/**
	 * The dimensions of each tile on the next piece preview.
	 */
	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1;
	
	/**
	 * The width of the shading on each tile on the next piece preview.
	 */
	private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1;
	
	/**
	 * The number of rows and columns in the preview window. Set to
	 * 5 because we can show any piece with some sort of padding.
	 */
	private static final int TILE_COUNT = 5;
	
	/**
	 * The center x of the next piece preview box.
	 */
	private static final int SQUARE_CENTER_X = 130;
	
	/**
	 * The center y of the next piece preview box.
	 */
	private static final int SQUARE_CENTER_Y = 65;
	
	/**
	 * The size of the next piece preview box.
	 */
	private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);
	
	/**
	 * The number of pixels used on a small insets (generally used for categories).
	 */
	private static final int SMALL_INSET = 20;
	
	/**
	 * The number of pixels used on a large insets.
	 */
	private static final int LARGE_INSET = 40;
	
	/**
	 * The y coordinate of the stats category.
	 */
	private static final int STATS_INSET = 175;
	
	/**
	 * The y coordinate of the controls category.
	 */
	private static final int CONTROLS_INSET = 270;
	
	/**
	 * The number of pixels to offset between each string.
	 */
	private static final int TEXT_STRIDE = 25;
	
	/**
	 * The small font.
	 */
	private static final Font SMALL_FONT = new Font("Times New Roman", Font.BOLD, 
                12);
	
	/**
	 * The large font.
	 */
	private static final Font LARGE_FONT = new Font("Verdana", Font.BOLD, 
                18);
        
        /**
	 * The Medium font.
	 */
	private static final Font MEDIUM_FONT = new Font("Arial", Font.BOLD, 
                12);
	
	/**
	 * The color to draw the text and preview box in.
	 */
	private static final Color DRAW_COLOR = new Color(128, 192, 128); 
	
	/**
	 * The Tetris instance.
	 */
	private Tetris tetTetis;
	
	/**
	 * Creates a new SidePanel and sets it's display properties.
	 * @param tetris The Tetris instance to use.
	 */
	public SidePanel(Tetris tetris) {
		this.tetTetis = tetris;
		
		setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.ORANGE);
	}
	
	@Override
	public void paintComponent(Graphics graGraphics) {
		super.paintComponent(graGraphics);
		
		//Set the color for drawing.
		graGraphics.setColor(DRAW_COLOR);
		
		/*
		 * This variable stores the current y coordinate of the string.
		 * This way we can re-order, add, or remove new strings if 
		 * necessary without needing to change the other strings.
		 */
		int iOffset;
		
		/*
		 * Draw the "Stats" category.
		 */
		graGraphics.setFont(LARGE_FONT);
                
		graGraphics.drawString("Stats", SMALL_INSET, 
                        iOffset = STATS_INSET);
                
		graGraphics.setFont(SMALL_FONT);
                
		graGraphics.drawString("Level: " + tetTetis.getLevel(), 
                        LARGE_INSET, iOffset += TEXT_STRIDE);
                
		graGraphics.drawString("Score: " + tetTetis.getScore(), 
                        LARGE_INSET, iOffset += TEXT_STRIDE);
		
		/*
		 * Draw the "Controls" category.
		 */
                graGraphics.setColor(Color.WHITE);
                
		graGraphics.setFont(LARGE_FONT);
                
		graGraphics.drawString("Controls", SMALL_INSET, 
                        iOffset = CONTROLS_INSET);
                
		graGraphics.setFont(SMALL_FONT);
                
		graGraphics.drawString("A - Move Left", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
		graGraphics.drawString("D - Move Right", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
		graGraphics.drawString("Q - Rotate Anticlockwise", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
		graGraphics.drawString("E - Rotate Clockwise", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
		graGraphics.drawString("S - Drop", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
		graGraphics.drawString("P - Pause Game", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
                graGraphics.drawString("G - Save Game", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
                
                graGraphics.drawString("C - Load Game", LARGE_INSET, 
                        iOffset += TEXT_STRIDE);
		
		/*
		 * Draw the next piece preview box.
		 */
                
                graGraphics.setColor(Color.BLACK);
                
		graGraphics.setFont(MEDIUM_FONT);
                
		graGraphics.drawString("Next Piece:", SMALL_INSET, 70);
                
		graGraphics.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, 
                        SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, 
                        SQUARE_SIZE * 2);
		
		/*
		 * Draw a preview of the next piece that will be spawned. The code is pretty much
		 * identical to the drawing code on the board, just smaller and centered, rather
		 * than constrained to a grid.
		 */
		TileType tilType = tetTetis.getNextPieceType();
                
		if(!tetTetis.isGameOver() && tilType != null) {
			/*
			 * Get the size properties of the current piece.
			 */
			int iCols = tilType.getCols();
			int iRows = tilType.getRows();
			int iDimension = tilType.getDimension();
		
			/*
			 * Calculate the top left corner (origin) of the piece.
			 */
			int iStartX = (SQUARE_CENTER_X - 
                                (iCols * TILE_SIZE / 2));
                        
			int iStartY = (SQUARE_CENTER_Y - 
                                (iRows * TILE_SIZE / 2));
		
			/*
			 * Get the insets for the preview. The default
			 * rotation is used for the preview, so we just use 0.
			 */
			int iTop = tilType.getTopInset(0);
                        
			int iLeft = tilType.getLeftInset(0);
		
			/*
			 * Loop through the piece and draw it's tiles onto 
                         * the preview.
			 */
			for(int iRow = 0; iRow < iDimension; iRow++) {
				for(int iCol = 0; iCol < iDimension; iCol++) {
					if(tilType.isTile(iCol, iRow, 0)) {
						drawTile(tilType, iStartX + 
                                                        ((iCol - iLeft) * 
                                                                TILE_SIZE), 
                                                        iStartY + ((iRow - iTop) 
                                                                * TILE_SIZE), 
                                                        graGraphics);
					}
				}
			}
		}
	}
	
	/**
	 * Draws a tile onto the preview window.
	 * @param tilType The type of tile to draw.
	 * @param iX The x coordinate of the tile.
	 * @param iY The y coordinate of the tile.
	 * @param graGraphics The graphics object.
	 */
	private void drawTile(TileType tilType, int iX, int iY, 
                Graphics graGraphics) {
		/*
		 * Fill the entire tile with the base color.
		 */
		graGraphics.setColor(tilType.getBaseColor());
                
		graGraphics.fillRect(iX, iY, TILE_SIZE, TILE_SIZE);
		
		/*
		 * Fill the bottom and right edges of the tile with the dark 
                 * shading color.
		 */
                
		graGraphics.setColor(tilType.getDarkColor());
                
		graGraphics.fillRect(iX, iY + TILE_SIZE - SHADE_WIDTH, 
                        TILE_SIZE, SHADE_WIDTH);
                
		graGraphics.fillRect(iX + TILE_SIZE - SHADE_WIDTH, iY, 
                        SHADE_WIDTH, TILE_SIZE);
		
		/*
		 * Fill the top and left edges with the light shading. 
		 *  We draw a single line for each row or column rather than 
		 *  a rectangle so that we can draw a nice looking 
                 * diagonal where the light and dark shading meet.
		 */
		graGraphics.setColor(tilType.getLightColor());
		for(int iI = 0; iI < SHADE_WIDTH; iI++) {
                    
			graGraphics.drawLine(iX, iY + iI, iX + 
                                TILE_SIZE - iI - 1, iY + iI);
                        
			graGraphics.drawLine(iX + iI, iY, iX 
                                + iI, iY + TILE_SIZE - iI - 1);
		}
	}
	
}
