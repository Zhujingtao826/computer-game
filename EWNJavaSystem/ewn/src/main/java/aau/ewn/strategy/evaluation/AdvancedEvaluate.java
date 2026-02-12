package aau.ewn.strategy.evaluation;

import java.util.List;
import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;

public class AdvancedEvaluate extends EvaluationFunction {

    // 改进的位置价值表
    private final double[][] POSITION_VALUE_RED = {
            {0.0, 1.2, 1.5, 1.2, 1.0},
            {1.2, 2.5, 3.0, 2.8, 2.0},
            {1.5, 3.0, 6.0, 5.0, 3.0},
            {1.2, 2.8, 5.0, 8.0, 6.0},
            {1.0, 2.0, 3.0, 6.0, 10.0}
    };

    private final double[][] POSITION_VALUE_BLUE = {
            {10.0, 6.0, 3.0, 2.0, 1.0},
            {6.0, 8.0, 5.0, 2.8, 1.2},
            {3.0, 5.0, 6.0, 3.0, 1.5},
            {2.0, 2.8, 3.0, 2.5, 1.2},
            {1.0, 1.2, 1.5, 1.2, 0.0}
    };

    // 移动性价值
    private final double MOBILITY_WEIGHT = 0.3;
    private final double THREAT_WEIGHT = 1.2;
    private final double DEFENSE_WEIGHT = 0.8;
    private final double CENTER_CONTROL_WEIGHT = 1.5;
    private final double ATTACK_WEIGHT = 2.0;

    @Override
    public double getValue(ChessBoard board, PieceType type) {
        double totalScore = 0.0;

        // 1. 基础位置价值
        totalScore += calculatePositionValue(board, type);

        // 2. 移动性评估
        totalScore += calculateMobility(board, type) * MOBILITY_WEIGHT;

        // 3. 威胁与攻击评估
        totalScore += calculateThreats(board, type) * THREAT_WEIGHT;

        // 4. 防御稳定性
        totalScore += calculateDefense(board, type) * DEFENSE_WEIGHT;

        // 5. 中心控制
        totalScore += calculateCenterControl(board, type) * CENTER_CONTROL_WEIGHT;

        // 6. 攻击潜力
        totalScore += calculateAttackPotential(board, type) * ATTACK_WEIGHT;

        // 7. 游戏阶段调整
        totalScore *= getGamePhaseMultiplier(board, type);

        return totalScore;
    }

    private double calculatePositionValue(ChessBoard board, PieceType type) {
        double positionValue = 0.0;
        List<Byte> pieces = board.getPieces(type);

        for (Byte piece : pieces) {
            int[] position = board.getPointByPiece(piece);
            List<Byte> dices = board.getDicesByPiece(piece);
            int diceCount = dices.size();

            double baseValue = (type == PieceType.RED) ?
                    POSITION_VALUE_RED[position[0]][position[1]] :
                    POSITION_VALUE_BLUE[position[0]][position[1]];

            // 骰子数量加权
            positionValue += baseValue * diceCount * (1 + 0.1 * (diceCount - 1));
        }

        return positionValue;
    }

    private double calculateMobility(ChessBoard board, PieceType type) {
        double mobility = 0.0;
        List<Byte> pieces = board.getPieces(type);

        for (Byte piece : pieces) {
            int[] position = board.getPointByPiece(piece);
            List<Byte> dices = board.getDicesByPiece(piece);
            int diceCount = dices.size();

            // 计算可能的移动方向
            int possibleMoves = countPossibleMoves(board, position, diceCount, type);
            mobility += possibleMoves * 0.5;
        }

        return mobility;
    }

    private double calculateThreats(ChessBoard board, PieceType type) {
        double threatValue = 0.0;
        PieceType opponent = (type == PieceType.RED) ? PieceType.BLUE : PieceType.RED;

        List<Byte> myPieces = board.getPieces(type);
        List<Byte> opponentPieces = board.getPieces(opponent);

        // 计算我对对手的威胁
        for (Byte myPiece : myPieces) {
            int[] myPos = board.getPointByPiece(myPiece);
            List<Byte> myDices = board.getDicesByPiece(myPiece);
            int myDiceCount = myDices.size();

            for (Byte oppPiece : opponentPieces) {
                int[] oppPos = board.getPointByPiece(oppPiece);
                List<Byte> oppDices = board.getDicesByPiece(oppPiece);
                int oppDiceCount = oppDices.size();

                double threat = calculateThreatLevel(myPos, oppPos, myDiceCount, oppDiceCount);
                threatValue += threat;
            }
        }

        return threatValue;
    }

    private double calculateThreatLevel(int[] myPos, int[] oppPos, int myDiceCount, int oppDiceCount) {
        int distance = Math.abs(myPos[0] - oppPos[0]) + Math.abs(myPos[1] - oppPos[1]);

        // 距离越近威胁越大
        if (distance <= 2) {
            double baseThreat = 3.0 / (distance + 1);

            // 骰子数量优势增加威胁
            if (myDiceCount > oppDiceCount) {
                baseThreat *= 1.5;
            }

            return baseThreat;
        }

        return 0.0;
    }

    private double calculateDefense(ChessBoard board, PieceType type) {
        double defenseValue = 0.0;
        List<Byte> pieces = board.getPieces(type);
        PieceType opponent = (type == PieceType.RED) ? PieceType.BLUE : PieceType.RED;

        for (Byte piece : pieces) {
            int[] position = board.getPointByPiece(piece);
            List<Byte> dices = board.getDicesByPiece(piece);

            // 检查是否有友军支援
            boolean hasSupport = hasFriendlySupport(board, position, type);
            if (hasSupport) {
                defenseValue += 1.0;
            }

            // 检查是否处于安全位置
            boolean isSafe = isPositionSafe(board, position, type, opponent);
            if (isSafe) {
                defenseValue += 0.5;
            }
        }

        return defenseValue;
    }

    private double calculateCenterControl(ChessBoard board, PieceType type) {
        double control = 0.0;

        // 中心区域定义
        int[][] centerPositions = {{1,1}, {1,2}, {1,3}, {2,1}, {2,2}, {2,3}, {3,1}, {3,2}, {3,3}};

        for (int[] pos : centerPositions) {
            int piece = board.getBoard()[pos[0]][pos[1]];
            if (piece != 0) {
                if ((type == PieceType.RED && piece < 20) ||
                        (type == PieceType.BLUE && piece > 20)) {
                    control += 2.0;
                }
            }
        }

        return control;
    }

    private double calculateAttackPotential(ChessBoard board, PieceType type) {
        double attackPotential = 0.0;
        PieceType opponent = (type == PieceType.RED) ? PieceType.BLUE : PieceType.RED;

        List<Byte> myPieces = board.getPieces(type);
        int[] opponentHome = (type == PieceType.RED) ? new int[]{4,4} : new int[]{0,0};

        for (Byte piece : myPieces) {
            int[] position = board.getPointByPiece(piece);
            int distanceToHome = Math.abs(position[0] - opponentHome[0]) +
                    Math.abs(position[1] - opponentHome[1]);

            // 距离对手大本营越近，攻击潜力越大
            attackPotential += (8 - distanceToHome) * 0.5;
        }

        return attackPotential;
    }

    private double getGamePhaseMultiplier(ChessBoard board, PieceType type) {
        // 根据游戏阶段调整策略
        int totalPieces = board.getPieces(PieceType.RED).size() +
                board.getPieces(PieceType.BLUE).size();

        if (totalPieces >= 10) {
            return 1.0; // 开局阶段
        } else if (totalPieces >= 6) {
            return 1.1; // 中局阶段
        } else {
            return 1.3; // 残局阶段
        }
    }

    // 辅助方法实现
    private int countPossibleMoves(ChessBoard board, int[] position, int diceCount, PieceType type) {
        // 实现移动方向计数逻辑
        int moves = 0;
        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {-1,-1}, {1,-1}, {-1,1}};

        for (int[] dir : directions) {
            int newRow = position[0] + dir[0];
            int newCol = position[1] + dir[1];

            if (newRow >= 0 && newRow < 5 && newCol >= 0 && newCol < 5) {
                moves++;
            }
        }

        return Math.min(moves, diceCount); // 移动数受骰子数量限制
    }

    private boolean hasFriendlySupport(ChessBoard board, int[] position, PieceType type) {
        int[][] adjacent = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] adj : adjacent) {
            int newRow = position[0] + adj[0];
            int newCol = position[1] + adj[1];

            if (newRow >= 0 && newRow < 5 && newCol >= 0 && newCol < 5) {
                int piece = board.getBoard()[newRow][newCol];
                if ((type == PieceType.RED && piece < 20 && piece > 0) ||
                        (type == PieceType.BLUE && piece > 20)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPositionSafe(ChessBoard board, int[] position, PieceType type, PieceType opponent) {
        // 检查位置是否安全（不被对手直接攻击）
        List<Byte> opponentPieces = board.getPieces(opponent);

        for (Byte oppPiece : opponentPieces) {
            int[] oppPos = board.getPointByPiece(oppPiece);
            int distance = Math.abs(position[0] - oppPos[0]) + Math.abs(position[1] - oppPos[1]);

            if (distance <= 2) { // 对手在攻击范围内
                List<Byte> oppDices = board.getDicesByPiece(oppPiece);
                if (oppDices.size() >= board.getDicesByPiece(
                        board.getBoard()[position[0]][position[1]]).size()) {
                    return false;
                }
            }
        }

        return true;
    }
}