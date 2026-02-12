package aau.ewn.strategy.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aau.ewn.board.ChessBoard;
import aau.ewn.board.PieceType;
import aau.ewn.game.GameState;
import aau.ewn.game.Move;
import aau.ewn.game.MoveGenerator;
import aau.ewn.strategy.evaluation.AdvancedEvaluate;

public class AdvancedMoveStrategy extends MoveStrategy {

    private static final int SEARCH_DEPTH = 3;
    private static final double POSITIVE_INFINITY = 9999999.0;
    private static final double NEGATIVE_INFINITY = -9999999.0;

    private AdvancedEvaluate evaluator;
    private Random random;

    public AdvancedMoveStrategy() {
        this.evaluator = new AdvancedEvaluate();
        this.random = new Random();
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {
        ChessBoard board = gameState.getCurrentBoard();
        PieceType player = gameState.getCurrentPlayer().getTurn();

        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);

        if (legalMoves.isEmpty()) {
            return null;
        }

        List<Move> bestMoves = new ArrayList<>();
        double bestValue = NEGATIVE_INFINITY;

        // Alpha-Beta搜索寻找最佳移动
        for (Map.Entry<Move, ChessBoard> entry : legalMoves.entrySet()) {
            Move move = entry.getKey();
            ChessBoard newBoard = entry.getValue();

            double value = alphaBetaMinimax(newBoard, SEARCH_DEPTH - 1, NEGATIVE_INFINITY, POSITIVE_INFINITY, false, player);

            // 记录搜索信息
            if (value > bestValue) {
                bestValue = value;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (Math.abs(value - bestValue) < 0.001) {
                // 值相近的移动都保留，增加随机性避免模式化
                bestMoves.add(move);
            }
        }

        // 设置策略信息
        this.value = bestValue;
        this.maxDepth = SEARCH_DEPTH;

        // 从最佳移动中随机选择一个（避免完全确定性）
        if (!bestMoves.isEmpty()) {
            return bestMoves.get(random.nextInt(bestMoves.size()));
        }

        // 备用方案：如果没有找到最佳移动，返回第一个合法移动
        return legalMoves.keySet().iterator().next();
    }

    private double alphaBetaMinimax(ChessBoard board, int depth, double alpha, double beta,
                                    boolean maximizingPlayer, PieceType originalPlayer) {

        // 终止条件：达到深度限制或游戏结束
        if (depth == 0 || isTerminal(board)) {
            return evaluator.getValue(board, originalPlayer);
        }

        PieceType currentPlayer = maximizingPlayer ? originalPlayer : getOpponent(originalPlayer);

        if (maximizingPlayer) {
            double maxEval = NEGATIVE_INFINITY;
            List<ChessBoard> childBoards = generateAllChildBoards(board, currentPlayer);

            // 对子状态进行排序以提高剪枝效率
            childBoards = orderChildBoards(childBoards, originalPlayer, true);

            for (ChessBoard child : childBoards) {
                double eval = alphaBetaMinimax(child, depth - 1, alpha, beta, false, originalPlayer);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break; // Beta剪枝
                }
            }
            return maxEval;
        } else {
            double minEval = POSITIVE_INFINITY;
            List<ChessBoard> childBoards = generateAllChildBoards(board, currentPlayer);

            // 对子状态进行排序以提高剪枝效率
            childBoards = orderChildBoards(childBoards, originalPlayer, false);

            for (ChessBoard child : childBoards) {
                double eval = alphaBetaMinimax(child, depth - 1, alpha, beta, true, originalPlayer);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) {
                    break; // Alpha剪枝
                }
            }
            return minEval;
        }
    }

    private List<ChessBoard> generateAllChildBoards(ChessBoard board, PieceType player) {
        List<ChessBoard> childBoards = new ArrayList<>();
        List<Byte> pieces = board.getPieces(player);

        // 为每个棋子的每个可能移动生成子状态
        for (Byte piece : pieces) {
            // 使用现有的getLegalMovesByPiece方法
            Map<Move, ChessBoard> moves = MoveGenerator.getLegalMovesByPiece(board, piece);

            for (ChessBoard childBoard : moves.values()) {
                childBoards.add(childBoard);
            }
        }

        // 如果没有子状态（可能由于规则限制），返回原始棋盘
        if (childBoards.isEmpty()) {
            childBoards.add(board.clone());
        }

        return childBoards;
    }

    private boolean isTerminal(ChessBoard board) {
        return board.isWin(PieceType.RED) || board.isWin(PieceType.BLUE);
    }

    private PieceType getOpponent(PieceType player) {
        return (player == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
    }

    // 快速评估版本（用于浅层搜索或排序）
    private double quickEvaluate(ChessBoard board, PieceType player) {
        // 简化的快速评估，只计算基础位置价值
        double score = 0.0;
        List<Byte> myPieces = board.getPieces(player);
        List<Byte> oppPieces = board.getPieces(getOpponent(player));

        // 棋子数量优势
        score += (myPieces.size() - oppPieces.size()) * 5.0;

        // 总骰子数优势
        int myTotalDice = 0;
        for (Byte piece : myPieces) {
            myTotalDice += board.getDicesByPiece(piece).size();
        }

        int oppTotalDice = 0;
        for (Byte piece : oppPieces) {
            oppTotalDice += board.getDicesByPiece(piece).size();
        }

        score += (myTotalDice - oppTotalDice) * 2.0;

        return score;
    }

    // 移动排序方法（提高Alpha-Beta剪枝效率）
    private List<ChessBoard> orderChildBoards(List<ChessBoard> childBoards, PieceType originalPlayer, boolean maximizing) {
        // 根据快速评估对子状态进行排序
        List<ChessBoard> sortedBoards = new ArrayList<>(childBoards);

        sortedBoards.sort((b1, b2) -> {
            double score1 = quickEvaluate(b1, originalPlayer);
            double score2 = quickEvaluate(b2, originalPlayer);

            if (maximizing) {
                return Double.compare(score2, score1); // 降序（好的状态在前）
            } else {
                return Double.compare(score1, score2); // 升序（差的状态在前）
            }
        });

        return sortedBoards;
    }

    @Override
    public void processEnemyMove(Move move) {
        // 可以在这里记录对手的移动，用于模式识别
    }

    @Override
    public void processStart(GameState gameState, PieceType myTurn) {
        // 游戏开始时的初始化
    }

    @Override
    public void processBack(GameState gameState, Move move) {
        // 处理回退移动
    }

    @Override
    public void processEnd() {
        // 游戏结束时的清理工作
    }

    // 简化版本 - 如果上面的版本太复杂，可以使用这个简化版
    public Move getMoveSimple(GameState gameState, byte dice) {
        ChessBoard board = gameState.getCurrentBoard();
        PieceType player = gameState.getCurrentPlayer().getTurn();

        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);

        if (legalMoves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        double bestValue = NEGATIVE_INFINITY;

        // 直接评估每个移动的结果
        for (Map.Entry<Move, ChessBoard> entry : legalMoves.entrySet()) {
            Move move = entry.getKey();
            ChessBoard newBoard = entry.getValue();

            double value = evaluator.getValue(newBoard, player);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        this.value = bestValue;
        this.maxDepth = 1; // 只搜索一层

        return bestMove;
    }
}