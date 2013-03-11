public class Block {

	// �L�[�{�[�h�̑����萔�Ƃ��Ē�`����
	private static final char LEFT = 'A';
	private static final char RIGHT = 'S';
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

	// �i�s�����̐��ʂ̕�����Ԃ�
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

	// �i�s�����̍��O���̕�����Ԃ�
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

	// �i�s�����̉E�O���̕�����Ԃ�
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
		// �O�ǂ�`��
		drawWall();

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
			// �i�s�����̍��E���O�ɕ���������ꍇ�͌������ɔ��]����
			if (scanLeft(ballX, ballY, ballWay) != ' ' && scanRight(ballX, ballY, ballWay) != ' ') {
				ballWay = backTurn(ballWay);
			}
			// �i�s�����̍��O�ɕ���������E�O�ɕ����������ꍇ�͉E�ɔ��]����
			else if (scanLeft(ballX, ballY, ballWay) != ' ') {
				ballWay = rightTurn(ballWay);
			}
			// �i�s�����̉E�O�ɕ��������荶�O�ɕ����������ꍇ�͉E�ɔ��]����
			else if (scanRight(ballX, ballY, ballWay) != ' ') {
				ballWay = leftTurn(ballWay);
			}
			// �i�s�����̍��E���O�ɕ������������ʂɕ�������ꍇ�͌������ɔ��]����
			else if (scanFront(ballX, ballY, ballWay) != ' ') {
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
			if (Console.pressed(LEFT)) {
				barX -= 2;
			}
			else if (Console.pressed(RIGHT)) {
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
