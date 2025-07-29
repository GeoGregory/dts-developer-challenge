from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///tasks.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

class Task(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(200), nullable=False)
    description = db.Column(db.String(500))
    status = db.Column(db.String(50), nullable=False)
    due_date = db.Column(db.DateTime, nullable=False)

    def __repr__(self):
        return f'<Task {self.id} - {self.title}>'

@app.route('/')
def index():
    search_id = request.args.get('search_id')
    tasks = []
    not_found = False

    if search_id:
        if search_id.isdigit():
            task = Task.query.filter_by(id=int(search_id)).first()
            if task:
                tasks = [task]
            else:
                not_found = True
        else:
            not_found = True
    else:
        tasks = Task.query.all()

    return render_template('index.html', tasks=tasks, not_found=not_found)

@app.route('/create')
def create_page():
    return render_template('create.html')

@app.route('/create', methods=['POST'])
def create():
    title = request.form['title'].strip()
    description = request.form.get('description', '').strip()
    status = request.form['status']
    due_date_str = request.form['dueDate']

    from datetime import datetime
    errors = []

    # Validate title
    if not title or title.isspace():
        errors.append("Title is required and cannot be just spaces.")

    # Optional: disallow special characters
    import re
    if re.search(r"[^\w\s\-.,]", title):
        errors.append("Title contains invalid special characters.")
    if description and re.search(r"[^\w\s\-.,]", description):
        errors.append("Description contains invalid special characters.")

    # Validate due date
    try:
        due_date = datetime.strptime(due_date_str, '%Y-%m-%dT%H:%M')
        if due_date < datetime.now():
            errors.append("Due date/time must be in the future.")
    except ValueError:
        errors.append("Invalid due date format.")

    if errors:
        for error in errors:
            flash(error, 'error')
        return render_template('create.html', form=request.form)

    # All good, create the task in DB
    new_task = Task(
        title=title,
        description=description,
        status=status,
        due_date=due_date
    )
    db.session.add(new_task)
    db.session.commit()

    return redirect(url_for('index'))

@app.route('/delete/<int:task_id>')
def delete(task_id):
    task = Task.query.get_or_404(task_id)
    db.session.delete(task)
    db.session.commit()
    return redirect(url_for('index'))

@app.route('/update/<int:task_id>', methods=['POST'])
def update(task_id):
    task = Task.query.get_or_404(task_id)
    task.status = request.form['status']
    db.session.commit()
    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True)