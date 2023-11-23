import express from 'express';
import mongoose from 'mongoose';
import morgan from 'morgan';
import cors from 'cors';
import { notFoundError, errorHandler } from './middlewares/error-handler.js';
import produitRoutes from './routes/produit.js';
import quizRoutes from './routes/quiz.js';
import questionRoutes from './routes/question.js';
import resultatQuizRoutes from './routes/resultatQuiz.js';
import userRoutes from './routes/user.js'; // Assurez-vous que le chemin du fichier est correct

import dechetsRoutes from './routes/dechets.js';
import demandeRoutes from './routes/demande.js';
import eventRoutes from './routes/event.js';
import CommentRoute from './routes/comment.js';
import commandeRoutes from './routes/commande.js';
import ephemeralKeyRouter from './routes/ephemeralKey.js';

import favproduitRoutes from './routes/favproduit.js'

//auth

import bodyParser from 'body-parser';
import authController from './controllers/authController.js';
///
const app = express();
app.use(bodyParser.json());
const port = process.env.PORT || 9090;
const databaseName = 'GreenWorld';
const db_url = process.env.DB_URL || `mongodb://127.0.0.1:27017`;

mongoose.set('debug', true);
mongoose.Promise = global.Promise;

mongoose
  .connect(`${db_url}/${databaseName}`)
  .then(() => {
    console.log(`Connected to ${databaseName}`);
  })
  .catch(err => {
    console.log(err);
  });

  app.use(cors());
  app.use(morgan('dev'));
  app.use(express.json());
  app.use(express.urlencoded({ extended: true }));
  app.use('/img', express.static('public/images'));
  app.use('/user', userRoutes); 

  app.use('/produit', produitRoutes);
  app.use('/auth', authController);
  app.use('/dechets', dechetsRoutes); 
  app.use('/demande', demandeRoutes);
  app.use('/quiz', quizRoutes);
  app.use('/question', questionRoutes);
  app.use('/resultatQuiz', resultatQuizRoutes);
  app.use('/event', eventRoutes);
  app.use('/commande', commandeRoutes);
  app.use('/ephemeral-key', ephemeralKeyRouter); 
  app.use('/favproduit', favproduitRoutes);
  app.use('/comment', CommentRoute)
  
  app.use(notFoundError);
  app.use(errorHandler);
 
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}/`);
});