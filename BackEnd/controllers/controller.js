const experss = require('express')
const userModal = require('../models/user')
const informationModal = require('../models/information')
const session = require('express-session')
const app = experss()



app.get('/', async (req, res) => {
    try {
        const infor = await informationModal.find().sort({ _id: -1 });
        // Sử dụng _id với giá trị -1 để sắp xếp theo thứ tự giảm dần của objectId.
        // Điều này sẽ đảm bảo bản ghi mới nhất được thêm vào sẽ hiển thị lên đầu tiên.

        res.json(infor);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Đã xảy ra lỗi khi lấy dữ liệu" });
    }
});
app.get('/Post/:id', async (req, res) => {

    const id = req.params.id
    try {
        const infor = await informationModal.find({ idauthor: id }).sort({ _id: -1 });
        // Sử dụng _id với giá trị -1 để sắp xếp theo thứ tự giảm dần của objectId.
        // Điều này sẽ đảm bảo bản ghi mới nhất được thêm vào sẽ hiển thị lên đầu tiên.

        res.json(infor);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Đã xảy ra lỗi khi lấy dữ liệu" });
    }
});

//hien thi chi tiet
app.get('/detail/:id', async (req, res) => {
    const id = req.params.id;
    console.log("id:", id);
    const infor = await informationModal.findById(id);
    res.json(infor);
});

//dang ky tai khoan
app.post('/signUp', async (req, res) => {
    const { username, password, fullname, email } = req.body;
    if (!username || !password || !fullname || !email) {
        return res.json({ message: "Vui lòng nhập đầy đủ thông tin " });
    }

    //kiểm tra xem username đã được đky chưa
    const checkUser = await userModal.findOne({ username })
    if (checkUser) {
        return res.json({ message: "Tên tài khoản đã được đăng ký " });
    }

    // Tạo đối tượng người dùng mới và lưu vào cơ sở dữ liệu 
    const newUser = new userModal({ username, password, fullname, email })
    try {
        await newUser.save();
        return res.json({ message: "Done" });
    } catch (error) {
        console.log(error);
    }
})

//dang nhap
app.post('/signIn', async (req, res) => {
    const { username, password } = req.body
    if (!username || !password) {
        return res.json({ message: "Vui lòng nhập đầy đủ thông tin " });
    }
    const checkUser = await userModal.findOne({ username });
    if (checkUser) {
        if (password === checkUser.password) {
            return res.json({ message: "Done", user: checkUser.fullname });

        } else {
            return res.json({ message: "Sai mật khẩu" });
        }

    } else {
        return res.json({ message: "Sai thông tin tài khoản " });
    }
})

app.get('/account/:id', async (req, res) => {
    console.log("OK");
    try {
        const username = req.params.id
        const infor = await userModal.findOne({ username: username });
        res.json(infor)
    } catch (error) {
        console.error(error)
        res.status(500).send("Lỗi")
    }
})

//them bai viet
app.post('/createStory', async (req, res) => {
    const { image, title, content, author, idauthor, date } = req.body
    if (!image || !title || !content) {
        return res.json({ message: "Vui lòng nhập đầy đủ thông tin " });
    }

    // Tạo đối tượng   mới và lưu vào cơ sở dữ liệu 
    const newUser = new informationModal({ image, title, content, author, idauthor, date })
    try {
        await newUser.save();
        return res.json({ message: "Thêm thành công " });
    } catch (error) {
        console.log(error);
    }
})

app.post('/editStory/:id', async (req, res) => {
    const id = req.params.id

    const story = {
        image: req.body.image,
        title: req.body.title,
        content: req.body.content,
    }
    if (!story.image || !story.title || !story.content) {
        return res.json({ message: "Vui lòng nhập đầy đủ thông tin " });
    }
    try {
        const update = await informationModal.findByIdAndUpdate(id, story)
        if (!update) {
            return res.status(404).render('404');
        }
        // Nếu cần, bạn có thể trả về dữ liệu đã cập nhật sau khi sửa bài viết.
        return res.json({ message: "Sửa bài viết thành công", updatedData: update });
    } catch (error) {
        console.log(error);
        return res.status(500).send(error.message);
    }
})

//xoa bai viet
app.post('/delete/:id', async (req, res) => {
    const id = req.params.id

    informationModal.findByIdAndRemove(id)
        .then((data) => {
            if (data) {
                res
                    .status(200)
                    .json({ message: "Dữ liệu đã được xóa thành công", data: data });
            } else {
                res.status(404).json({ error: "Không tìm thấy dữ liệu" });
            }
        })
        .catch((error) => {
            res.status(500).json({ error: "Đã xảy ra lỗi khi xóa dữ liệu" });
        });
})

//tim theo ten 
app.post('/search/:id', async (req, res) => {
    const sp = req.params.id
    try {
        const users = await informationModal.find({ title: { $regex: sp, $options: 'i' } });
        console.log(sp);
        console.log(users);
        res.json(users)

    } catch (err) {
        console.error(err);
        res.sendStatus(500);
    }
});

//them comment
app.post('/comment/:id', async (req, res) => {
    const id = req.params.id;


    const { idPerson, fullName, cmt } = req.body

    const title = await informationModal.findById(id)

    const comment = {
        idPerson: idPerson,
        fullName: fullName,
        cmt: cmt
    }

    try {
        title.comment.push(comment);//them vao mang comment
        await title.save()
        return res.json({ message: "Thêm bình luận thành công " });

    } catch (error) {
        console.error(error);
    }
})

//sua comment
app.post('/editcomment/:idPosts/:idPerson/:idComment', async (req, res) => {
    const idPosts = req.params.idPosts;
    const idPerson = req.params.idPerson;
    const idComment = req.params.idComment;
    const cmt = req.body.cmt;

    try {
        const posts = await informationModal.findById(idPosts);
        if (!posts) {
            return res.json({ error: "Không tìm thấy bài đăng." });
        }

        // Tìm bình luận dựa trên idPerson và idComment
        const comment = posts.comment.find(cmt => cmt.idPerson === idPerson && cmt._id == idComment);
        if (!comment) {
            return res.json({ error: "Không tìm thấy bình luận." });
        }

        // Cập nhật nội dung mới cho bình luận
        comment.cmt = cmt;

        // Lưu các thay đổi vào cơ sở dữ liệu 
        await posts.save();

        res.json({ message: "Thành công" });
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: "Lỗi" });
    }
});

//xoa comment
app.post('/deletecomment/:idPosts/:idPerson/:idComment', async (req, res) => {

    const idPosts = req.params.idPosts;
    const idPerson = req.params.idPerson;
    const idComment = req.params.idComment;
    console.log("id bài: " + idPosts + "id Người BL: " + idPerson + "id Cmt " + idComment);
    try {
        const posts = await informationModal.findById(idPosts);
        if (!posts) {
            return res.status(404).json({ error: "Không tìm thấy bài đăng." });
        }

        // Tìm bình luận dựa trên idPerson và idComment
        const comment = posts.comment.findIndex(cmt => cmt.idPerson === idPerson && cmt._id == idComment);
        if (comment === -1) {
            return res.status(404).json({ error: "Không tìm thấy bình luận." });
        }

        // Xóa bình luận khỏi mảng comment
        posts.comment.splice(comment, 1);

        // Lưu các thay đổi vào cơ sở dữ liệu 
        await posts.save();

        res.json({ message: "Xóa bình luận thành công" });
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: "Đã xảy ra lỗi trong quá trình xử lý." });
    }
});

module.exports = app
