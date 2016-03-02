package org.psnbtech;

import java.awt.Color;

/**
 * The {@code PieceType} enum describes the properties of the various pieces 
 * that can be used in the game.
 * @author Brendan Jones
 *
 */
public enum TileType {

	/**
	 * Piece TypeI.
	 */
	TypeI(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, 
                BoardPanel.COLOR_MAX), 4, 4, 1, new boolean[][] {
                    
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	
	/**
	 * Piece TypeJ.
	 */
	TypeJ(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, 
                BoardPanel.COLOR_MAX), 3, 3, 2, new boolean[][] {
                    
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeL.
	 */
	TypeL(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), 3, 3, 
                2, new boolean[][] {
                    
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeO.
	 */
	TypeO(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, 
                BoardPanel.COLOR_MIN), 2, 2, 2, new boolean[][] {
                    
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	
	/**
	 * Piece TypeS.
	 */
	TypeS(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, 
                BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
                    
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeT.
	 */
	TypeT(new Color(128, BoardPanel.COLOR_MIN, 128), 3, 3, 2, 
                new boolean[][] {
                    
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Piece TypeZ.
	 */
	TypeZ(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, 
                BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
                    
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	});
		
	/**
	 * The base color of tiles of this type.
	 */
	private Color clrBaseColor;
	
	/**
	 * The light shading color of tiles of this type.
	 */
	private Color clrLightColor;
	
	/**
	 * The dark shading color of tiles of this type.
	 */
	private Color clrDarkColor;
	
	/**
	 * The column that this type spawns in.
	 */
	private int iSpawnCol;
	
	/**
	 * The row that this type spawns in.
	 */
	private int iSpawnRow;
	
	/**
	 * The dimensions of the array for this piece.
	 */
	private int iDimension;
	
	/**
	 * The number of iRows in this piece. (Only valid when rotation is 
         * 0 or 2,
	 * but it's fine since we're only using it for displaying the next piece
	 * preview, which uses rotation 0).
	 */
	private int iRows;
	
	/**
	 * The number of columns in this piece. (Only valid when rotation is 
         * 0 or 2,
	 * but it's fine since we're only using it for displaying the next piece
	 * preview, which uses rotation 0).
	 */
	private int iCols;
	
	/**
	 * The tiles for this piece. Each piece has an array of tiles for each 
         * rotation.
	 */
	private boolean[][] matbTiles;
	
	/**
	 * Creates a new TileType.
	 * @param clrColor The base color of the tile.
	 * @param iDimension The dimensions of the tiles array.
	 * @param iCols The number of columns.
	 * @param iRows The number of iRows.
	 * @param tiles The tiles.
	 */
	private TileType(Color clrColor, int iDimension, int iCols, int iRows, 
                boolean[][] tiles) {
            
		this.clrBaseColor = clrColor;
		this.clrLightColor = clrColor.brighter();
		this.clrDarkColor = clrColor.darker();
		this.iDimension = iDimension;
		this.matbTiles = tiles;
		this.iCols = iCols;
		this.iRows = iRows;
		
		this.iSpawnCol = 5 - (iDimension >> 1);
		this.iSpawnRow = getTopInset(0);
	}
	
	/**
	 * Gets the base color of this type.
	 * @return The base color.
	 */
	public Color getBaseColor() {
            
		return clrBaseColor;
	}
	
	/**
	 * Gets the light shading color of this type.
	 * @return The light color.
	 */
	public Color getLightColor() {
            
		return clrLightColor;
	}
	
	/**
	 * Gets the dark shading color of this type.
	 * @return The dark color.
	 */
	public Color getDarkColor() {
            
		return clrDarkColor;
	}
	
	/**
	 * Gets the iDimension of this type.
	 * @return The iDimension.
	 */
	public int getDimension() {
            
		return iDimension;
	}
	
	/**
	 * Gets the spawn column of this type.
	 * @return The spawn column.
	 */
	public int getSpawnColumn() {
            
		return iSpawnCol;
	}
	
	/**
	 * Gets the spawn row of this type.
	 * @return The spawn row.
	 */
	public int getSpawnRow() {
            
		return iSpawnRow;
	}
	
	/**
	 * Gets the number of iRows in this piece. (Only valid when rotation 
	 * is 0 or 2, but it's fine since this is only used for the preview
         * which uses rotation 0).
	 * @return The number of iRows.
	 */
	public int getRows() {
            
		return iRows;
	}
	
	/**
	 * Gets the number of columns in this piece. (Only valid when rotation 
	 * is 0 or 2, but it's fine since this is only used for the preview
         * which uses rotation 0).
	 * @return The number of columns.
	 */
	public int getCols() {
            
		return iCols;
	}
	
	/**
	 * Checks to see if the given coordinates and rotation contain a tile.
	 * @param iX The x coordinate of the tile.
	 * @param iY The y coordinate of the tile.
	 * @param iRotation The rotation to check in.
	 * @return Whether or not a tile resides there.
	 */
	public boolean isTile(int iX, int iY, int iRotation) {
		return matbTiles[iRotation][iY * iDimension + iX];
	}
	
	/**
	 * The left inset is represented by the number of empty columns 
	 * on the left side of the array for the given rotation.
	 * @param iRotation The rotation.
	 * @return The left inset.
	 */
	public int getLeftInset(int iRotation) {
		/*
		 * Loop through from left to right until we find a tile then 
                 * return
		 * the column.
		 */
		for(int iX = 0; iX < iDimension; iX++) {
			for(int iY = 0; iY < iDimension; iY++) {
				if(isTile(iX, iY, iRotation)) {
					return iX;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The right inset is represented by the number of empty columns on the 
	 * left side of the array for the given rotation.
	 * @param iRotation The rotation.
	 * @return The right inset.
	 */
	public int getRightInset(int iRotation) {
		/*
		 * Loop through from right to left until we find a tile then 
                 * return
		 * the column.
		 */
		for(int iX = iDimension - 1; iX >= 0; iX--) {
			for(int iY = 0; iY < iDimension; iY++) {
				if(isTile(iX, iY, iRotation)) {
					return iDimension - iX;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The left inset is represented by the number of empty iRows on the top
            side of the array for the given rotation.
	 * @param iRotation The rotation.
	 * @return The top inset.
	 */
	public int getTopInset(int iRotation) {
		/*
		 * Loop through from top to bottom until we find a tile then 
                 * return
		 * the row.
		 */
		for(int iY = 0; iY < iDimension; iY++) {
			for(int iX = 0; iX < iDimension; iX++) {
				if(isTile(iX, iY, iRotation)) {
					return iY;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The botom inset is represented by the number of empty iRows on the 
         * bottom
            side of the array for the given rotation.
	 * @param iRotation The rotation.
	 * @return The bottom inset.
	 */
	public int getBottomInset(int iRotation) {
		/*
		 * Loop through from bottom to top until we find a tile then 
                 * return
		 * the row.
		 */
		for(int iY = iDimension - 1; iY >= 0; iY--) {
			for(int iX = 0; iX < iDimension; iX++) {
				if(isTile(iX, iY, iRotation)) {
					return iDimension - iY;
				}
			}
		}
		return -1;
	}
	
}
