package game;

import model.Checker;

public interface BoardSubject {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyNewChecker(Checker checker);
	public void notifyMove(Checker checker, int toPoint);
	public void notifyPlayer(int player);
}
