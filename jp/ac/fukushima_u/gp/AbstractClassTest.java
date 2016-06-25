package jp.ac.fukushima_u.gp;

class Goods{
	public String getGoodsName() {
		String name = "test";
		return name;
	}
}

class NearGoods extends Goods{
	public int getPrice() {
		return 1000;
	}
}

class FarGoods extends Goods{
	public int getPrice() {
		return 5000;
	}
}