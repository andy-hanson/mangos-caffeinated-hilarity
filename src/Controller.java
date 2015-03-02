import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Controller implements KeyListener {
	final int K_UP = 38, K_LEFT = 37, K_RIGHT = 39, K_DOWN = 40, K_Z = 90, K_X = 88, K_C = 67, K_ENTER = 10, K_Q = 81, K_P = 80;
	int otherKeyDown; //When a wierd key is pressed, this is set to it!

	boolean up, down, left, right, A, B, X, Select, Start, Pause;
	//UDLR - arrow keys - move Mango
	//A - Z - jump
	//B - X  - Attack!
	//X - C - not used
	//Select - Enter - player cheat, flies up -
	//Start - Q - restarts level
	//Pause - P - pauses game

	boolean upPressedThisFrame;
	public Controller() {
		up=down=left=right=A=Select=upPressedThisFrame=false;
	}
	void upDown() {
		if (!up) upPressedThisFrame = true;
		up = true;
	}
	void upUp() { up = false; }
	void leftDown() { left = true; }
	void leftUp() { left = false; }
	void rightDown() { right = true; }
	void rightUp() { right = false; }
	void downDown() { down = true; }
	void downUp() { down = false; }
	void ADown() { A = true; }
	void AUp() { A = false; }
	void BDown() { B = true; }
	void BUp() { B = false; }
	void XDown() { X = true; }
	void XUp() { X = false; }
	void SelectDown() { Select = true; }
	void SelectUp() { Select = false; }
	void StartDown() { Start = true; }
	void StartUp() { Start = false; }
	void PauseDown() { Pause = true; }
	void PauseUp() { Pause = false; }
	public boolean anyButtonDown() { return up || down || left || right || A || B || X || Start || Select; }

	public void compute() { upPressedThisFrame = false; }

	public void keyTyped(KeyEvent e) { /*pass; keys handled by below functions*/ }

	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case K_UP:		upDown();		break;
			case K_LEFT:	leftDown();		break;
			case K_RIGHT:	rightDown();	break;
			case K_DOWN:	downDown();		break;
			case K_Z: 		ADown();			break;
			case K_X: 		BDown();			break;
			case K_C: 		XDown();			break;
			case K_Q:		StartDown();	break;
			case K_ENTER:	SelectDown();	break;
			case K_P:		PauseDown();	break;
			default: otherKeyDown = e.getKeyCode();
		}
	}
   public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case K_UP:		upUp();			break;
			case K_LEFT:	leftUp();		break;
			case K_RIGHT:	rightUp();		break;
			case K_DOWN:	downUp();		break;
			case K_Z:		AUp();			break;
			case K_X: 		BUp();			break;
			case K_C: 		XUp();			break;
			case K_Q:		StartUp();		break;
			case K_ENTER:	SelectUp();		break;
			case K_P:		PauseUp();	break;
			default: otherKeyDown = 0;
		}
	}
}