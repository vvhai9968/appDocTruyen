const express = require('express')
const expressHbs = require('express-handlebars')
const bodyparser = require('body-parser')
const mongooes = require('mongoose')
const url = 'mongodb+srv://haivvph24197:06102003@cluster0.yer5dgk.mongodb.net/ThuSignInSignUp?retryWrites=true&w=majority'
const userController = require('./controllers/controller')

const session = require('express-session');
const app = express()

app.use(session({
    secret: 'secret-key',
    resave: false,
    saveUninitialized: false
}));

//cau hinh chay file .hbs
app.engine('.hbs', expressHbs.engine({
    defaultLayout: null,
    extname: '.hbs'
}))
app.set('view engine', '.hbs')

//cau hinh cho body-parser
app.use(bodyparser.urlencoded({
    extended: true
}))
app.use(bodyparser.json())

mongooes.connect(url, { useUnifiedTopology: true, useNewUrlParser: true }).then(() => {
    console.log("successful connection");
}).catch((err) => {
    console.error("connection error", err);
})


app.use(userController)
const port = 9999
app.listen(port, () => {
    console.log(`server running ${port}`);
})
