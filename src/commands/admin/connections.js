const Commands = require("../../structures/Commands");

module.exports = class extends Commands {

    constructor(client) {
        super(client);
        this.client = client;

        this.name = "connections";
        this.aliases = ["c"];

        this.description = "Shows the current active connections";
        this.category = "admin";

        this.usage = "connections";
        this.options = [];

        this.requiredPermissions = [];

        this.enabled = true;
        this.ownersOnly = true;
        this.ignoreSlash = true;
    }

    async runAsMessage(message) {

        const playersData = await this.client.database.db("guilds").collection("players").find({}).toArray()
        return message.reply({ content: `\`${playersData.length}\`` });

    }

}