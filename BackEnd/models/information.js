const mongoes = require('mongoose');
const userSchema = mongoes.Schema({
    image: { type: String },
    title: { type: String },
    content: { type: String },
    author: { type: String },
    idauthor: { type: String },
    date: { type: String },
    comment: {
        type: [{
            idPerson: String,
            fullName: String,
            cmt: String

        }]
    }
})
const user = mongoes.model('information', userSchema)
module.exports = user