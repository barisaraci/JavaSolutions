
import java.util.Random;

import javax.swing.JOptionPane;

public class Board {
	
	private Entity entityMatrix[][];
	private Random random = new Random();
	
	private int dimension, difficulty, numberOfRightMoves, numberOfMoves;
	private int rowNumbers[], colNumbers[];
	
	private final int[] DIMENSIONS = {8, 12, 16, 20};
	private final int[] NUMBER_OF_TENTS_REGARDING_DIMENSION = {12, 28, 51, 80};
	
	private final char TENT = 'T';
	private final char TREE = 'E';
	
	private final String MESSAGE_VICTORY = "You won";
	
	public Board() {
		
	}
	
	public void create(int dimension) {
		this.dimension = dimension;
		difficulty = getDifficulty(dimension);
		entityMatrix = new Entity[dimension][dimension];
		rowNumbers = new int[dimension];
		colNumbers = new int[dimension];
		prepareBoard();
	}
	
	private void prepareBoard() {
		addEntities();
		calculateNumberOfTents();
	}
	
	private void addEntities() {
		int currentRow = 0;
		int numberOfTentsPlaced = NUMBER_OF_TENTS_REGARDING_DIMENSION[difficulty];
		
		while (numberOfTentsPlaced > 0) {
			int x, y;
			do {
				x = random.nextInt(dimension);
				y = currentRow;
				currentRow = (currentRow + 1 < dimension) ? currentRow += 1 : 0;
			} while (!isCellAvailableToPutEntity(x, y, TENT));
			if (numberOfTentsPlaced > 0) {
				Entity tent = new Tent(x, y, false, true);
				setTreeOfTentAtPosition(x, y);
				addEntity(tent);
				numberOfTentsPlaced--;
			}
		}
	}
	
	private void setTreeOfTentAtPosition(int x, int y) {
		int newPositions[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
		int newX, newY;
		do {
			int randomPos = random.nextInt(4);
			newX = x + newPositions[randomPos][0];
			newY = y + newPositions[randomPos][1];
		} while (!isPositionValid(newX, newY) || !isCellAvailableToPutEntity(newX, newY, TREE));
		Entity tree = new Tree(newX, newY);
		addEntity(tree);
	}
	
	private boolean isCellAvailableToPutEntity(int x, int y, char entityType) {
		if (entityType == TREE) {
			return entityMatrix[x][y] == null;
		} else if (entityType == TENT) {
			return getNumberOfEntitiesAroundPosition(x, y, TENT) < 1 && entityMatrix[x][y] == null;
		}
		
		return false;
	}
	
	private int getNumberOfEntitiesAroundPosition(int x, int y, char entityType) {
		int numberOfEntities = 0;
		
		if (isPositionValid(x, y + 1) && entityMatrix[x][y + 1] != null && entityMatrix[x][y + 1].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x + 1, y + 1) && entityMatrix[x + 1][y + 1] != null && entityMatrix[x + 1][y + 1].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x + 1, y) && entityMatrix[x + 1][y] != null && entityMatrix[x + 1][y].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x + 1, y - 1) && entityMatrix[x + 1][y - 1] != null && entityMatrix[x + 1][y - 1].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x, y - 1) && entityMatrix[x][y - 1] != null && entityMatrix[x][y - 1].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x - 1, y - 1) && entityMatrix[x - 1][y - 1] != null && entityMatrix[x - 1][y - 1].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x - 1, y) && entityMatrix[x - 1][y] != null && entityMatrix[x - 1][y].type == entityType)
			numberOfEntities++;
		if (isPositionValid(x - 1, y + 1) && entityMatrix[x - 1][y + 1] != null && entityMatrix[x - 1][y + 1].type == entityType)
			numberOfEntities++;
		
		return numberOfEntities;
	}
	
	private boolean isPositionValid(int x, int y) {
		if (x < 0 || x >= dimension || y < 0 || y >= dimension)
			return false;
		
		return true;
	}
	
	private void calculateNumberOfTents() {
		for (int y = 0; y < dimension; y++) {
			int totalNumberOfTents = 0;
			
			for (int x = 0; x < dimension; x++) {
				if (entityMatrix[x][y] != null && entityMatrix[x][y].type == TENT)
					totalNumberOfTents++;
			}
			
			rowNumbers[y] = totalNumberOfTents;
		}
		
		for (int x = 0; x < dimension; x++) {
			int totalNumberOfTents = 0;
			
			for (int y = 0; y < dimension; y++) {
				if (entityMatrix[x][y] != null && entityMatrix[x][y].type == TENT)
					totalNumberOfTents++;
			}
			
			colNumbers[x] = totalNumberOfTents;
		}
	}
	
	public boolean isTentCreated(int x, int y) {
		if (isPositionValid(x, y) && entityMatrix[x][y] == null) {
			Entity tent = new Tent(x, y, true, false);
			addEntity(tent);
			numberOfMoves++;
			return true;
		} else if (isPositionValid(x, y) && entityMatrix[x][y] != null && entityMatrix[x][y].getType() == TENT && entityMatrix[x][y].isAnswer() && !entityMatrix[x][y].isVisible()) {
			Entity tent = entityMatrix[x][y];
			tent.setVisible(true);
			numberOfMoves++;
			numberOfRightMoves++;
			checkWin();
			return true;
		}
		
		return false;
	}
	
	private void addEntity(Entity entity) {
		if (isPositionValid(entity.getX(), entity.getY())) {
			entityMatrix[entity.getX()][entity.getY()] = entity;
		}
	}
	
	private void checkWin() {
		if (numberOfRightMoves == NUMBER_OF_TENTS_REGARDING_DIMENSION[difficulty] && numberOfMoves == numberOfRightMoves) {
			JOptionPane.showMessageDialog(null, MESSAGE_VICTORY);
		}
	}
	
	public void restart() {
		numberOfRightMoves = 0;
		numberOfMoves = 0;
		for (int col = 0; col < dimension; col++) {
			for (int row = 0; row < dimension; row++) {
				if (entityMatrix[col][row] != null && entityMatrix[col][row].getType() == TENT) {
					if(entityMatrix[col][row].isAnswer()) {
						entityMatrix[col][row].setVisible(false);
					} else {
						entityMatrix[col][row] = null;
					}
				}
			}
		}
	}
	
	private int getDifficulty(int dimension) {
		if(dimension == DIMENSIONS[0]) {
			return 0;
		} else if(dimension == DIMENSIONS[1]) {
			return 1;
		} else if(dimension == DIMENSIONS[2]) {
			return 2;
		} else if(dimension == DIMENSIONS[3]) {
			return 3;
		}
		return -1;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public int[] getRowNumbers() {
		return rowNumbers;
	}
	
	public int[] getColNumbers() {
		return colNumbers;
	}
	
	public Entity[][] getEntityMatrix() {
		return entityMatrix;
	}
	
}
