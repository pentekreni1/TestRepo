package model;

public class Checker {

	public final int color;
	private int position;
	
	public Checker(int color) {
		this.color = color;
	}
	
	public void setPosition(int pos){
		position = pos;
	}

	public int getPosition(){
		return position;
	}

	@Override
	public boolean equals(Object obj) {
		final Checker other = (Checker) obj;
		if (this.color == other.color && this.position == other.position) {
			return true;
		}
		return false;
	}
}
