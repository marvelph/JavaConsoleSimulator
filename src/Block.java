public class Block {

	// キーボードの操作を定数として定義する
	private static final char LEFT = 'A';
	private static final char RIGHT = 'S';
	// ボールの飛ぶ方向を定数として定義する
	private static final int LEFT_UP = 0;
	private static final int RIGHT_UP = 1;
	private static final int RIGHT_DOWN = 2;
	private static final int LEFT_DOWN = 3;

	// 外壁を描く
	private static void drawWall() {
		// 上の壁を描く
		for (int x = 0; x < Console.WIDTH; x++) {
			Console.locate(x, 0);
			Console.print('#');
		}
		// 左右の壁を描く
		// 0行目は必要無い事に注意
		for (int y = 1; y < Console.HEIGHT; y++) {
			Console.locate(0, y);
			Console.print('#');
			Console.locate(Console.WIDTH - 1, y);
			Console.print('#');
		}
	}

	// 進行方向の後ろ方向を返す
	private static int backTurn(int way) {
		switch (way) {
		case LEFT_UP:
			return RIGHT_DOWN;
		case RIGHT_UP:
			return LEFT_DOWN;
		case RIGHT_DOWN:
			return LEFT_UP;
		case LEFT_DOWN:
			return RIGHT_UP;
		default:
			return way;
		}
	}

	// 進行方向の左方向を返す
	private static int leftTurn(int way) {
		switch (way) {
		case LEFT_UP:
			return LEFT_DOWN;
		case RIGHT_UP:
			return LEFT_UP;
		case RIGHT_DOWN:
			return RIGHT_UP;
		case LEFT_DOWN:
			return RIGHT_DOWN;
		default:
			return way;
		}
	}

	// 進行方向の右方向を返す
	private static int rightTurn(int way) {
		switch (way) {
		case LEFT_UP:
			return RIGHT_UP;
		case RIGHT_UP:
			return RIGHT_DOWN;
		case RIGHT_DOWN:
			return LEFT_DOWN;
		case LEFT_DOWN:
			return LEFT_UP;
		default:
			return way;
		}
	}

	// 進行方向の正面の文字を返す
	private static char scanFront(int x, int y, int way) {
		switch (way) {
		case LEFT_UP:
			return Console.scan(x - 1, y - 1);
		case RIGHT_UP:
			return Console.scan(x + 1, y - 1);
		case RIGHT_DOWN:
			return Console.scan(x + 1, y + 1);
		case LEFT_DOWN:
			return Console.scan(x - 1, y + 1);
		default:
			return ' ';
		}
	}

	// 進行方向の左前側の文字を返す
	private static char scanLeft(int x, int y, int way) {
		switch (way) {
		case LEFT_UP:
			return Console.scan(x - 1, y);
		case RIGHT_UP:
			return Console.scan(x, y - 1);
		case RIGHT_DOWN:
			return Console.scan(x + 1, y);
		case LEFT_DOWN:
			return Console.scan(x, y + 1);
		default:
			return ' ';
		}
	}

	// 進行方向の右前側の文字を返す
	private static char scanRight(int x, int y, int way) {
		switch (way) {
		case LEFT_UP:
			return Console.scan(x, y - 1);
		case RIGHT_UP:
			return Console.scan(x + 1, y);
		case RIGHT_DOWN:
			return Console.scan(x, y + 1);
		case LEFT_DOWN:
			return Console.scan(x - 1, y);
		default:
			return ' ';
		}
	}

	public static void main(String[] args) {
		// 外壁を描く
		drawWall();

		// バーの横方向の中心座標を変数宣言する
		int barX = 10;
		// ボールの座標と方向を変数宣言する
		int ballX = 10;
		int ballY = 22;
		int ballWay = RIGHT_UP;

		// ゲームオーバーにならない限り無限に実行する
		while (true) {
			// 一旦ボールを消す
			Console.locate(ballX, ballY);
			Console.print(' ');

			// 周りの文字に衝突している場合は進行方向を変更する
			// 進行方向の左右両前に文字がある場合は後ろ方向に反転する
			if (scanLeft(ballX, ballY, ballWay) != ' ' && scanRight(ballX, ballY, ballWay) != ' ') {
				ballWay = backTurn(ballWay);
			}
			// 進行方向の左前に文字があり右前に文字が無い場合は右に反転する
			else if (scanLeft(ballX, ballY, ballWay) != ' ') {
				ballWay = rightTurn(ballWay);
			}
			// 進行方向の右前に文字があり左前に文字が無い場合は右に反転する
			else if (scanRight(ballX, ballY, ballWay) != ' ') {
				ballWay = leftTurn(ballWay);
			}
			// 進行方向の左右両前に文字が無く正面に文字ある場合は後ろ方向に反転する
			else if (scanFront(ballX, ballY, ballWay) != ' ') {
				ballWay = backTurn(ballWay);
			}

			// 現在の方向にボールの位置を移動する
			switch (ballWay) {
			case LEFT_UP:
				ballX--;
				ballY--;
				break;
			case RIGHT_UP:
				ballX++;
				ballY--;
				break;
			case RIGHT_DOWN:
				ballX++;
				ballY++;
				break;
			case LEFT_DOWN:
				ballX--;
				ballY++;
				break;
			}

			// 再びボールを描く
			Console.locate(ballX, ballY);
			Console.print('@');

			// 一旦バーを消す
			Console.locate(barX - 2, 23);
			Console.print("     ");

			// キーボードが押されていたらバーの位置を移動する
			if (Console.pressed(LEFT)) {
				barX -= 2;
			}
			else if (Console.pressed(RIGHT)) {
				barX += 2;
			}

			// バーが左右の壁にめり込まないように位置を補正する
			if (barX <= 2) {
				barX = 3;
			}
			else if (barX >= Console.WIDTH - 3) {
				barX = Console.WIDTH - 4;
			}

			// 再びバーを表示する
			Console.locate(barX - 2, 23);
			Console.print("<===>");

			// 表示したまま少し待つ
			Console.sleep(50);
		}
	}

}
