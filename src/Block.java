public class Block {

	// �L�[�{�[�h�̑����萔�Ƃ��Ē�`����
	private static final char FAST_LEFT = 'A';
	private static final char LEFT = 'S';
	private static final char RIGHT = 'D';
	private static final char FAST_RIGHT = 'F';
	// �{�[���̔�ԕ�����萔�Ƃ��Ē�`����
	private static final int LEFT_UP = 0;
	private static final int RIGHT_UP = 1;
	private static final int RIGHT_DOWN = 2;
	private static final int LEFT_DOWN = 3;

	// �O�ǂ�`��
	private static void drawWall() {
		// ��̕ǂ�`��
		for (int x = 0; x < Console.WIDTH; x++) {
			Console.locate(x, 0);
			Console.print('#');
		}
		// ���E�̕ǂ�`��
		// 0�s�ڂ͕K�v�������ɒ���
		for (int y = 1; y < Console.HEIGHT; y++) {
			Console.locate(0, y);
			Console.print('#');
			Console.locate(Console.WIDTH - 1, y);
			Console.print('#');
		}
	}

	// �j��ł���u���b�N��p�ӂ���
	private static void drawBlocks() {
		for (int y = 4; y < 8; y++) {
			for (int x = 1; x < Console.WIDTH - 2; x++) {
				Console.locate(x, y);
				Console.print('=');
			}
		}
	}

	// �i�s�����̌�������Ԃ�
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

	// �i�s�����̍�������Ԃ�
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

	// �i�s�����̉E������Ԃ�
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

	// �i�s�����̐��ʂ�x���W��Ԃ�
	private static int xFront(int x, int way) {
		switch (way) {
		case LEFT_UP:
			return x - 1;
		case RIGHT_UP:
			return x + 1;
		case RIGHT_DOWN:
			return x + 1;
		case LEFT_DOWN:
			return x -1;
		default:
			return x;
		}
	}

	// �i�s�����̐��ʂ�y���W��Ԃ�
	private static int yFront(int y, int way) {
		switch (way) {
		case LEFT_UP:
			return y - 1;
		case RIGHT_UP:
			return y - 1;
		case RIGHT_DOWN:
			return y + 1;
		case LEFT_DOWN:
			return y + 1;
		default:
			return y;
		}
	}

	// �i�s�����̍��O��x���W��Ԃ�
	private static int xLeft(int x, int way) {
		switch (way) {
		case LEFT_UP:
			return x - 1;			
		case RIGHT_DOWN:
			return x + 1;
		default:
			return x;
		}
	}

	// �i�s�����̍��O��y���W��Ԃ�
	private static int yLeft(int y, int way) {
		switch (way) {
		case RIGHT_UP:
			return y - 1;
		case LEFT_DOWN:
			return y + 1;
		default:
			return y;
		}
	}

	// �i�s�����̉E�O��x���W��Ԃ�
	private static int xRight(int x, int way) {
		switch (way) {
		case RIGHT_UP:
			return x + 1;
		case LEFT_DOWN:
			return x - 1;
		default:
			return x;
		}
	}

	// �i�s�����̉E�O��y���W��Ԃ�
	private static int yRight(int y, int way) {
		switch (way) {
		case LEFT_UP:
			return y - 1;			
		case RIGHT_DOWN:
			return y + 1;
		default:
			return y;
		}
	}

	// �i�s�����̐��ʂ̕�����Ԃ�
	private static char scanFront(int x, int y, int way) {
		x = xFront(x, way);			
		y = yFront(y, way);
		return Console.scan(x, y);
	}

	// �i�s�����̍��O���̕�����Ԃ�
	private static char scanLeft(int x, int y, int way) {
		x = xLeft(x, way);			
		y = yLeft(y, way);
		return Console.scan(x, y);
	}

	// �i�s�����̉E�O���̕�����Ԃ�
	private static char scanRight(int x, int y, int way) {
		x = xRight(x, way);			
		y = yRight(y, way);
		return Console.scan(x, y);
	}

	// �������󂹂�Ȃ�Ύw����W�̃u���b�N������
	private static void hit(int x, int y) {
		if (Console.scan(x, y) == '=') {
			Console.locate(x, y);
			Console.print(' ');
		}
	}

	// �������󂹂�Ȃ�ΐi�s�����̐��ʂɂ���u���b�N������
	private static void hitFront(int x, int y, int way) {
		x = xFront(x, way);			
		y = yFront(y, way);
		hit(x, y);
	}

	// �������󂹂�Ȃ�ΐi�s�����̍��O�ɂ���u���b�N������
	private static void hitLeft(int x, int y, int way) {
		x = xLeft(x, way);			
		y = yLeft(y, way);
		hit(x, y);
	}

	// �������󂹂�Ȃ�ΐi�s�����̉E�O�ɂ���u���b�N������
	private static void hitRight(int x, int y, int way) {
		x = xRight(x, way);			
		y = yRight(y, way);
		hit(x, y);
	}

	public static void main(String[] args) {
		// �O�ǂ�`��
		drawWall();

		// �j��ł���u���b�N��p�ӂ���
		drawBlocks();

		// �o�[�̉������̒��S���W��ϐ��錾����
		int barX = 10;
		// �{�[���̍��W�ƕ�����ϐ��錾����
		int ballX = 10;
		int ballY = 22;
		int ballWay = RIGHT_UP;

		// �Q�[���I�[�o�[�ɂȂ�Ȃ����薳���Ɏ��s����
		while (true) {
			// ��U�{�[��������
			Console.locate(ballX, ballY);
			Console.print(' ');

			// ����̕����ɏՓ˂��Ă���ꍇ�͐i�s������ύX����
			// �i�s�����̍��E���O�ɕ���������ꍇ��
			if (scanLeft(ballX, ballY, ballWay) != ' ' && scanRight(ballX, ballY, ballWay) != ' ') {
				// �������󂹂�Ȃ�ΐi�s�����̍��E���O�ɂ��镶��������
				hitLeft(ballX, ballY, ballWay);
				hitRight(ballX, ballY, ballWay);

				// �������ɔ��]����
				ballWay = backTurn(ballWay);
			}
			// �i�s�����̍��O�ɕ���������E�O�ɕ����������ꍇ��
			else if (scanLeft(ballX, ballY, ballWay) != ' ') {
				// �������󂹂�Ȃ�ΐi�s�����̍��O�ɂ��镶��������
				hitLeft(ballX, ballY, ballWay);

				// �E�ɔ��]����
				ballWay = rightTurn(ballWay);
			}
			// �i�s�����̉E�O�ɕ��������荶�O�ɕ����������ꍇ��
			else if (scanRight(ballX, ballY, ballWay) != ' ') {
				// �������󂹂�Ȃ�ΐi�s�����̉E�O�ɂ��镶��������
				hitRight(ballX, ballY, ballWay);

				// �E�ɔ��]����
				ballWay = leftTurn(ballWay);
			}
			// �i�s�����̍��E���O�ɕ������������ʂɕ�������ꍇ��
			else if (scanFront(ballX, ballY, ballWay) != ' ') {
				// �������󂹂�Ȃ�ΐi�s�����̐��ʂɂ��镶��������
				hitFront(ballX, ballY, ballWay);

				// �������ɔ��]����
				ballWay = backTurn(ballWay);
			}

			// ���݂̕����Ƀ{�[���̈ʒu���ړ�����
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

			// �Ăу{�[����`��
			Console.locate(ballX, ballY);
			Console.print('@');

			// ��U�o�[������
			Console.locate(barX - 2, 23);
			Console.print("     ");

			// �L�[�{�[�h��������Ă�����o�[�̈ʒu���ړ�����
			if (Console.pressed(FAST_LEFT)) {
				barX -= 2;
			}
			else if (Console.pressed(LEFT)) {
				barX -= 1;
			}
			else if (Console.pressed(RIGHT)) {
				barX += 1;
			}
			else if (Console.pressed(FAST_RIGHT)) {
				barX += 2;
			}

			// �o�[�����E�̕ǂɂ߂荞�܂Ȃ��悤�Ɉʒu��␳����
			if (barX <= 2) {
				barX = 3;
			}
			else if (barX >= Console.WIDTH - 3) {
				barX = Console.WIDTH - 4;
			}

			// �Ăуo�[��\������
			Console.locate(barX - 2, 23);
			Console.print("<===>");

			// �\�������܂܏����҂�
			Console.sleep(50);
		}
	}

}
