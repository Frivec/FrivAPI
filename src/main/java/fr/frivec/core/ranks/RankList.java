package fr.frivec.core.ranks;

public enum RankList {
	
	ADMIN(100, "Admin", "§c[Admin]", ""),
	MOD(50, "Mod", "§9[Mod]", ""),
	DEV(90, "Dev", "§6[Dev]", ""),
	STAFF(10, "Staff", "§3[Staff]", ""),
	FRIEND(10, "ami", "§f[Ami]", ""),
	YT(30, "youtube", "§d[YouTube]", ""),
	VIP_PLUS(30, "vipplus", "§b[VIP+]", ""),
	VIP(10, "vip", "§a[VIP]", ""),
	PLAYER(0, "player", "§7", "");
	
	private int power;
	private String name,
					prefix,
					suffix;
	
	private RankList(int power, String name, String prefix, String suffix) {
	
		this.power = power;
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
	
	}
	
	public static RankList getRank(final String name) {
		
		for(RankList ranks : RankList.values())
			
			if(ranks.getName().equalsIgnoreCase(name))
				
				return ranks;
		
		return null;
		
	}
	
	public static RankList getRank(final int power) {
		
		for(RankList ranks : RankList.values())
			
			if(ranks.getPower() == power)
				
				return ranks;
		
		return null;
		
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
