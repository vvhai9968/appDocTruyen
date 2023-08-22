const mongoes = require('mongoose');
const userSchema = mongoes.Schema({
    username: { type: String },
    password: { type: String },
    fullname: { type: String },
    email: { type: String }
})
const user = mongoes.model('user',userSchema)
module.exports = user