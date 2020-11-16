// const { sequelize, Sequelize } = require(".");

// Sequelize : DB에서 조회 된 값을 객체로 전달해주는 ORM(object-relational mapping)

module.exports = (sequelize, Sequelize) => {
    const Chatlog = sequelize.define("chatlog", {
        message: {
            type: Sequelize.STRING
        },
        userid:{
            type: Sequelize.STRING
        },
        roomid:{
            type: Sequelize.STRING
        }
    });

    return Chatlog;
};